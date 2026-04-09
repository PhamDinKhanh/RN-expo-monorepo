package expo.modules.datasyncnativekotlin.sdk.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationV2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE users ADD COLUMN age INTEGER NOT NULL DEFAULT 0
            """.trimIndent(),
        )
    }
}
