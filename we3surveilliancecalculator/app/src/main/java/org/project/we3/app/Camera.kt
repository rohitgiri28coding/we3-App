package org.project.we3.app

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "camera_table")
data class Camera(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firestoreId: String = "",  // Firestore Document ID
    val name: String="",
    val unitPrice: Double=0.0,
    val detail: String="",
    val mrp: Double=0.0,
    val gst: Double=0.0,
    val quantity: Int=0
){
    // Firestore requires an empty constructor
    constructor() : this(0, "", "", 0.0, "", 0.0, 0.0, 0)
}

