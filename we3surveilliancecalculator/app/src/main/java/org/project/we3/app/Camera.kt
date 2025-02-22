package org.project.we3.app

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "camera_table")
data class Camera(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firestoreId: String = "",  // Firestore Document ID
    val name: String,
    val unitPrice: Double,
    val detail: String,
    val mrp: Double,
    val gst: Double,
    val quantity: Int
){
    // Firestore requires an empty constructor
    constructor() : this(0, "", "", 0.0, "", 0.0, 0.0, 0)
}

