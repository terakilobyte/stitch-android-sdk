package com.mongodb.stitch.core.services.mongodb.remote.sync.internal

import com.mongodb.MongoNamespace
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.stitch.core.internal.net.Event
import com.mongodb.stitch.core.services.mongodb.remote.ChangeEvent
import com.mongodb.stitch.core.services.mongodb.remote.RemoteDeleteResult
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult
import com.mongodb.stitch.core.services.mongodb.remote.internal.CoreRemoteMongoCollectionImpl
import org.bson.BsonDocument
import org.bson.BsonValue
import org.bson.Document
import java.io.Closeable
import java.lang.Exception

/**
 * Set fake version state to be included on updates.
 */
enum class TestVersionState {
    NONE, PREVIOUS, SAME, NEXT, NEW
}
/**
 * Testing context to test a data synchronizer.
 *
 * Should be served fresh only by the [SyncUnitTestHarness].
 *
 * Multiple instances of the testing context could result in
 * race conditions if not opened and closed properly.
 */
interface DataSynchronizerTestContext : Closeable {
    val namespace: MongoNamespace
    val clientKey: String
    val instanceKey: String
    val testDocument: BsonDocument
    val testDocumentId: BsonValue
    val testDocumentFilter: BsonDocument
    var updateDocument: BsonDocument

    val collectionMock: CoreRemoteMongoCollectionImpl<BsonDocument>
    var shouldConflictBeResolvedByRemote: Boolean
    var exceptionToThrowDuringConflict: Exception?

    val localClient: MongoClient

    /**
     * Whether or not we are online. Acts as a switch.
     */
    var isOnline: Boolean

    /**
     * Whether or not we are logged in. Acts as a switch.
     */
    var isLoggedIn: Boolean

    /**
     * A stream event to be consumed. Should be written to.
     */
    var nextStreamEvent: Event
    val dataSynchronizer: DataSynchronizer

    /**
     * An instance of the local synchronized collection.
     */
    val localCollection: MongoCollection<BsonDocument>

    /**
     * Reconfigure the dataSynchronizer.
     */
    fun reconfigure()

    /**
     * Wait for the data synchronizer to open streams.
     */
    fun waitForDataSynchronizerStreams()

    /**
     * Wait for an error to be emitted.
     */
    fun waitForError()

    /**
     * Wait for an event to be emitted.
     */
    fun waitForEvents(amount: Int = 1)

    /**
     * Reconfigure dataSynchronizer. Insert the contextual test document.
     */
    fun insertTestDocument()

    /**
     * Reconfigure dataSynchronizer. Update the contextual test document with
     * the contextual update document.
     */
    fun updateTestDocument(): UpdateResult

    /**
     * Reconfigure dataSynchronizer. Delete the contextual test document.
     */
    fun deleteTestDocument(): DeleteResult

    /**
     * Reconfigure dataSynchronizer. Do a sync pass.
     */
    fun doSyncPass()

    /**
     * Sets the pending writes for a particular synchronized document ID.
     */
    fun setPendingWritesForDocId(documentId: BsonValue, event: ChangeEvent<BsonDocument>)

    /**
     * Attempt to find the contextual test document locally.
     */
    fun findTestDocumentFromLocalCollection(): BsonDocument?

    /**
     * Verify the changeEventListener was called for the test document.
     */
    fun verifyChangeEventListenerCalledForActiveDoc(times: Int, vararg expectedChangeEvents: ChangeEvent<BsonDocument> = arrayOf())

    /**
     * Verify the errorListener was called for the test document.
     */
    fun verifyErrorListenerCalledForActiveDoc(times: Int, error: Exception? = null)

    /**
     * Verify the conflict handler was called for the test document.
     */
    fun verifyConflictHandlerCalledForActiveDoc(
        times: Int,
        expectedLocalConflictEvent: ChangeEvent<BsonDocument>? = null,
        expectedRemoteConflictEvent: ChangeEvent<BsonDocument>? = null
    )

    /**
     * Verify the stream function was called.
     */
    fun verifyWatchFunctionCalled(times: Int, expectedArgs: Document)

    /**
     * Verify dataSynchronizer.start() has been called.
     */
    fun verifyStartCalled(times: Int)

    /**
     * Verify dataSynchronizer.stop() has been called.
     */
    fun verifyStopCalled(times: Int)

    /**
     * Verify that the undo collection of the data synchronizer is empty.
     */
    fun verifyUndoCollectionEmpty()

    /**
     * Queue a pseudo-remote insert event to be consumed during R2L.
     */
    fun queueConsumableRemoteInsertEvent()

    /**
     * Queue a pseudo-remote update event to be consumed during R2L.
     */
    fun queueConsumableRemoteUpdateEvent(
        id: BsonValue = testDocumentId,
        document: BsonDocument = testDocument,
        versionState: TestVersionState = TestVersionState.NEXT
    )

    /**
     * Queue a pseudo-remote delete event to be consumed during R2L.
     */
    fun queueConsumableRemoteDeleteEvent()

    /**
     * Queue a pseudo-remote unknown event to be consumed during R2L.
     */
    fun queueConsumableRemoteUnknownEvent()

    /**
     * Mock an exception when inserting into the remote collection.
     */
    fun mockInsertException(exception: Exception)

    /**
     * Mock a result when updating on remote collection.
     */
    fun mockUpdateResult(remoteUpdateResult: RemoteUpdateResult)

    /**
     * Mock an exception when updating on the remote collection.
     */
    fun mockUpdateException(exception: Exception)

    /**
     * Mock a result when deleting on the remote collection.
     */
    fun mockDeleteResult(remoteDeleteResult: RemoteDeleteResult)

    /**
     * Mock an exception when deleting on the remote collection.
     */
    fun mockDeleteException(exception: Exception)
}
