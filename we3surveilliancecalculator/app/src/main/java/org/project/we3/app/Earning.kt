package org.project.we3.app

import java.util.UUID

data class Earning(
    val id: String = UUID.randomUUID().toString(),
    val firestoreId: String = "",
    val date: String = "",
    val totalEarning: Double=0.0,
    val prepayment: Double=0.0,
    val quotationId: String="",
    val pdfId: String = ""
){
    constructor(): this("", "", "", 0.0, 0.0, "")
}
