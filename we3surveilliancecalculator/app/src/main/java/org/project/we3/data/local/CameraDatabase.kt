package org.project.we3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.project.we3.data.model.Camera

@Database(entities = [Camera::class], version = 1, exportSchema = false)
abstract class CameraDatabase : RoomDatabase() {

    abstract fun cameraDao(): CameraDao

    companion object {
        @Volatile
        private var INSTANCE: CameraDatabase? = null

        fun getDatabase(context: Context): CameraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CameraDatabase::class.java,
                    "camera_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
