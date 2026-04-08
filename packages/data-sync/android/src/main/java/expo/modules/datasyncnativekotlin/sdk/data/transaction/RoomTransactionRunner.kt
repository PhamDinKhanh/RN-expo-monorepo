package expo.modules.datasyncnativekotlin.sdk.data.transaction

import androidx.room.withTransaction
import expo.modules.datasyncnativekotlin.sdk.data.local.database.AppDatabase

class RoomTransactionRunner(
    private val database: AppDatabase,
) : TransactionRunner {
    override suspend fun <R> run(block: suspend () -> R): R = database.withTransaction(block)
}
