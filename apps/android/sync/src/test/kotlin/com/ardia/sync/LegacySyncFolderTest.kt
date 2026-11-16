package com.ardia.sync

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LegacySyncFolderTest {
    @get:Rule
    val temporary = TemporaryFolder()

    @Test
    fun aPickedLocalParentFindsTheMacsArdiaSyncFolder() {
        val parent = temporary.newFolder()
        val legacy = File(parent, "Ardia Sync").apply { mkdirs() }

        val grant = LocalFolderAccess().open(SyncFolderPick.Folder(parent))

        assertEquals(SyncFolderLocation.Path(legacy.path, legacy.name), grant?.location)
    }

    @Test
    fun aPickedLocalParentPrefersPopulatedArdiaOverEmptyAeri() {
        val parent = temporary.newFolder()
        File(parent, "Aeri Sync").mkdirs()
        val legacy = File(parent, "Ardia Sync").apply { mkdirs() }
        File(legacy, "a".repeat(26)).mkdirs()

        val grant = LocalFolderAccess().open(SyncFolderPick.Folder(parent))

        assertEquals(SyncFolderLocation.Path(legacy.path, legacy.name), grant?.location)
    }

    @Test
    fun aPickedDocumentParentFindsTheMacsArdiaSyncFolder() {
        val tree = FakeDocumentTree()
        tree.add(tree.rootId, "Ardia Sync", directory = true)

        assertEquals("Ardia Sync", DocumentTreeStorage.locate(tree))
    }

    @Test
    fun aPickedDocumentParentPrefersPopulatedArdiaOverEmptyAeri() {
        val tree = FakeDocumentTree()
        tree.add(tree.rootId, "Aeri Sync", directory = true)
        val legacy = tree.add(tree.rootId, "Ardia Sync", directory = true)
        tree.add(legacy, "a".repeat(26), directory = true)

        assertEquals("Ardia Sync", DocumentTreeStorage.locate(tree))
    }
}
