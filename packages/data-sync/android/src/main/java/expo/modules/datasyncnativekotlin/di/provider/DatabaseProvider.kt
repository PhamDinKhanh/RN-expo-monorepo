package expo.modules.datasyncnativekotlin.di.provider

import android.content.Context
import androidx.room.Room
import expo.modules.datasyncnativekotlin.sdk.data.local.database.AppDatabase

fun provideRoomDatabase(context: Context): AppDatabase =
    Room
        .databaseBuilder(
            context,
            AppDatabase::class.java,
            "tablet_offline_sync.db",
        ).fallbackToDestructiveMigration()
        // Thêm các logic phức tạp
        .build()

fun providePokemonDao(database: AppDatabase) = database.pokemonDao()

fun provideOutboxDao(database: AppDatabase) = database.outboxDao()
