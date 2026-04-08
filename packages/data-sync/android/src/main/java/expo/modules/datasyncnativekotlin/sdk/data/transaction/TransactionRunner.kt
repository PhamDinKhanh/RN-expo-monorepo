package expo.modules.datasyncnativekotlin.sdk.data.transaction

interface TransactionRunner {
    suspend fun <R> run(block: suspend () -> R): R
}
