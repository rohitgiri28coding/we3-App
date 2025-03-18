package org.project.we3.app.db

import org.project.we3.app.Camera
import java.util.UUID

data class Quotation(
    var id : String = UUID.randomUUID().toString(),
    var firestoreId: String = "",
    var camera: List<Camera>,
    var quantity: List<Int>,
    var customerName: String="",
    var phoneNumber: String="",
    var dateGenerated: String="",
    var expiryDate: String="",
    var prepaymentAmount: Double = 0.0,
    var priority: Boolean = false,
    var extraDiscount: Boolean = false,
    var discountAmount: Double = 0.0,
    var isActive: Boolean = true,
    var completionDate: String = ""
){
    constructor(): this("","", emptyList(), emptyList(), "", "", "", "", 0.0, false, false, 0.0, true, "")
}
