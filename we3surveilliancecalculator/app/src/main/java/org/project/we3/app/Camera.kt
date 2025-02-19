package org.project.we3.app

data class Camera(
    val name: String = "",
    val unitPrice: Double = 0.0,
    val detail: String = "",
    val mrp: Double = 0.0,
    val gst: Double = 0.0,
    val quantity: Int = 0
) {
    // Firestore requires an empty constructor
    constructor() : this("", 0.0, "", 0.0, 0.0, 0)
}
