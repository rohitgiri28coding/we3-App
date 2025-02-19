package org.project.we3.app

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CameraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamera(camera: Camera)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cameras: List<Camera>)

    @Query("SELECT * FROM camera_table ORDER BY name ASC")
    fun getAllCameras(): Flow<List<Camera>>

    @Query("DELETE FROM camera_table")
    suspend fun clearAll()
}
