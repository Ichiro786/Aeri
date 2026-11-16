package com.ardia.sync

import com.ardia.sync.core.RecoveryPhrase
import java.util.UUID
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LegacyKeystoreCompatibilityTest {
    @Test
    fun theDefaultStoreReadsSecretsSealedBeforeTheRebrand() {
        val sandbox = Sandbox()
        val account = "legacy-upgrade-${UUID.randomUUID()}"
        val legacy = KeystoreKeyStore(sandbox.keys, "Ardia Sync Key")
        try {
            val entropy = RecoveryPhrase.generateEntropy()
            assertTrue(legacy.save(entropy, account))

            assertArrayEquals(entropy, KeystoreKeyStore(sandbox.keys).load(account))
        } finally {
            legacy.delete(account)
            sandbox.remove()
        }
    }
}
