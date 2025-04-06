package org.project.we3.data.model

import java.util.UUID

data class Quotation(
    var id : String = UUID.randomUUID().toString(),
    var firestoreId: String = "",
    var camera: List<Camera> = emptyList(),
    var quantity: List<Int> = emptyList(),
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
    constructor(): this(UUID.randomUUID().toString(),"", emptyList(), emptyList(), "", "", "", "", 0.0, false, false, 0.0, true, "")
}