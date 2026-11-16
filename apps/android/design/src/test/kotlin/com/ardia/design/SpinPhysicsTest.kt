package com.ardia.design

import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpinPhysicsTest {
    private val frame = 1f / 120f

    private fun runToRest(spin: SpinPhysics, limitSeconds: Float = 30f): Float {
        var elapsed = 0f
        while (spin.step(frame)) {
            elapsed += frame
            assertTrue("still spinning after $limitSeconds s", elapsed < limitSeconds)
            assertFalse(spin.angle.isNaN())
        }
        return elapsed
    }

    private fun flick(spin: SpinPhysics, degreesPerSecond: Float) {
        spin.grab(0)
        for (step in 1..8) spin.dragBy(degreesPerSecond * 0.008f, step * 8L)
        spin.release(64)
    }

    @Test
    fun `a flick carries the measured speed and coasts for several turns before resting upright`() {
        val spin = SpinPhysics()
        flick(spin, 1500f)
        assertEquals(1500f, spin.velocity, 20f)
        val seconds = runToRest(spin)
        assertTrue(seconds > 3f)
        assertTrue(abs(spin.angle) >= 3 * 360f)
        assertEquals(0f, spin.angle % 360f, 0f)
        assertEquals(0f, spin.velocity, 0f)
        assertFalse(spin.isMoving)
    }

    @Test
    fun `a backwards flick spins the other way and still lands upright`() {
        val spin = SpinPhysics()
        flick(spin, -900f)
        runToRest(spin)
        assertTrue(spin.angle <= -720f)
        assertEquals(0f, spin.angle % 360f, 0f)
    }

    @Test
    fun `speed only ever bleeds away once the logo is let go`() {
        val spin = SpinPhysics(uprightPull = 0f)
        spin.kick(1200f)
        var last = abs(spin.velocity)
        while (spin.step(frame)) {
            assertTrue(abs(spin.velocity) <= last + 1e-3f)
            last = abs(spin.velocity)
            if (last < 3f) break
        }
    }

    @Test
    fun `a finger that pauses before lifting leaves the logo to swing home without a fling`() {
        val spin = SpinPhysics()
        spin.grab(0)
        for (step in 1..5) spin.dragBy(12f, step * 8L)
        spin.release(400)
        assertEquals(0f, spin.velocity, 0f)
        runToRest(spin)
        assertEquals(0f, spin.angle, 0f)
    }

    @Test
    fun `touching a spinning logo stops it where it is`() {
        val spin = SpinPhysics()
        spin.kick(2000f)
        repeat(30) { spin.step(frame) }
        val held = spin.angle
        spin.grab(1_000)
        assertFalse(spin.step(frame))
        assertEquals(held, spin.angle, 0f)
        assertEquals(0f, spin.velocity, 0f)
    }

    @Test
    fun `flicks add up but never past the top speed`() {
        val spin = SpinPhysics()
        repeat(10) { spin.kick(900f) }
        assertEquals(3000f, spin.velocity, 0f)
    }

    @Test
    fun `left upside down it tips over and comes home`() {
        val spin = SpinPhysics()
        spin.grab(0)
        spin.dragBy(180f, 8)
        spin.release(500)
        runToRest(spin)
        assertEquals(0f, spin.angle % 360f, 0f)
    }

    @Test
    fun `the frame rate does not change where a flick ends up`() {
        val slow = SpinPhysics().also { it.kick(1400f) }
        val fast = SpinPhysics().also { it.kick(1400f) }
        while (slow.step(1f / 60f)) Unit
        while (fast.step(1f / 120f)) Unit
        assertEquals(slow.angle, fast.angle, 0f)
    }
}
