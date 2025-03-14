package org.project.we3.app.db

import org.project.we3.app.Camera

data class Quotation(
    var camera: List<Camera>,
    var quantity: List<Int>,
    var customerName: String="",
    var phoneNumber: String="",
    var dateGenerated: String="",
    var validity: String="",
    var prepaymentAmount: Double = 0.0,
    var priority: Boolean = false,
    var extraDiscount: Boolean = false,
    var discountAmount: Double = 0.0
)
