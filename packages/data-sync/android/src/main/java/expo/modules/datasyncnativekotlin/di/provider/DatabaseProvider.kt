package expo.modules.datasyncnativekotlin.di.provider

import android.content.Context
import androidx.room.Room
import expo.modules.datasyncnativekotlin.data.local.database.AppDatabase


fun provideRoomDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "tablet_offline_sync.db"
    )
        .fallbackToDestructiveMigration()
        // Thêm các logic phức tạp
        .build()
}

// Hàm hỗ trợ lấy DAO
fun provideOrderDao(database: AppDatabase) = database.userDao()
