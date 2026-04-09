package expo.modules.datasyncnativekotlin.di.provider

import android.content.Context
import androidx.room.Room
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncConfig
import expo.modules.datasyncnativekotlin.sdk.data.local.database.AppDatabase

fun provideRoomDatabase(
    context: Context,
    config: DataSyncConfig,
): AppDatabase =
    Room
        .databaseBuilder(
            context,
            AppDatabase::class.java,
            config.databaseName,
        ).fallbackToDestructiveMigration()
        .build()

fun providePokemonDao(database: AppDatabase) = database.pokemonDao()

fun provideOutboxDao(database: AppDatabase) = database.outboxDao()
