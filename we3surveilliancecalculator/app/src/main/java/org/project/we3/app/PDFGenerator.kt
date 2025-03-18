package org.project.we3.app

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import org.project.we3.app.db.Quotation
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.core.graphics.toColorInt

fun formatNumberIntoIndianNumber(number: Double): String {
    return NumberFormat.getInstance(Locale("en", "IN")).format(number)
}

private fun wrapTextToLines(text: String, width: Int =555, paint: Paint): List<String> {
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= width) {
            currentLine = testLine
        } else {
            lines.add(currentLine)
            currentLine = word
        }
    }
    if (currentLine.isNotEmpty()) lines.add(currentLine)

    return lines
}

fun convertNumberToWords(number: Double): String {
    if (number == 0.0) return "Zero Rupees Only"

    val integerPart = number.toLong()
    val decimalPart = ((number - integerPart) * 100).toLong()

    val words = StringBuilder()

    // Convert integer part to words
    words.append(numberToWords(integerPart))
    words.append(" Rupees")

    // Convert decimal part to words (if applicable)
    if (decimalPart > 0) {
        words.append(" and ")
        words.append(numberToWords(decimalPart))
        words.append(" Paise")
    }

    words.append(" Only")

    return words.toString().trim()
}

fun numberToWords(num: Long): String {
    if (num == 0L) return "Zero"

    val belowTwenty = arrayOf(
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
        "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    )

    val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    val thousands = arrayOf("", "Thousand", "Lakh", "Crore")

    var number = num
    val word = StringBuilder()

    val parts = mutableListOf<Long>()

    // Extract the numbers according to the Indian number system
    parts.add(number % 1000) // Get the last 3 digits (units, tens, hundreds)
    number /= 1000

    while (number > 0) {
        parts.add(number % 100) // Get the next 2 digits (thousands, lakhs, crores)
        number /= 100
    }

    for (i in parts.indices.reversed()) {
        val chunk = parts[i]
        if (chunk > 0) {
            val chunkWords = StringBuilder()
            if (chunk < 20) {
                chunkWords.append(belowTwenty.getOrNull(chunk.toInt()) ?: "")
            } else {
                val tensIndex = (chunk / 10).toInt()
                val onesIndex = (chunk % 10).toInt()
                chunkWords.append(tens.getOrNull(tensIndex) ?: "")
                if (onesIndex > 0) {
                    chunkWords.append(" ").append(belowTwenty.getOrNull(onesIndex) ?: "")
                }
            }
            if (i > 0) {
                chunkWords.append(" ").append(thousands.getOrNull(i) ?: "")
            }
            word.append(chunkWords).append(" ")
        }
    }

    return word.toString().trim()
}

fun createQuotationPDF(context: Context, quotation: Quotation) {
    try {
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val pageWidth = 595
        val pageHeight = 842
        val margin = 20f
        val maxContentHeight = pageHeight - 100f  // Leave space for footer

        val boldTypeface = Typeface.create("Arial", Typeface.BOLD)
        val regularTypeface = Typeface.create("Arial", Typeface.NORMAL)
        val blueColor = "#007AA5".toColorInt()
        val redColor = "#B93540".toColorInt()
        paint.textSize = 14f
        var currentY = 60f
        var totalAmount = 0.0
        var pageIndex = 1

        fun createNewPage(): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex).create()
            val page = pdfDocument.startPage(pageInfo)
            pageIndex++
            return page
        }

        var page = createNewPage()
        val canvas = page.canvas

        fun drawHeader() {
            val date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            paint.typeface = boldTypeface
            paint.color = redColor
            paint.textSize = 22f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("We3 Surveillance Camera Store", pageWidth / 2f, currentY, paint)
            currentY += 25f
            paint.color = blueColor

            paint.textSize = 15f
            canvas.drawText("Bakerganj, Patna", pageWidth / 2f, currentY, paint)
            currentY += 20f
            canvas.drawText("Phone No.: 9386673993", pageWidth / 2f, currentY, paint)
            currentY += 25f

            paint.color = Color.BLACK
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, paint)
            currentY += 25f
            canvas.drawText("PDF Generated On: $date", pageWidth - 140f, currentY, paint)
            currentY += 30f
        }

        fun drawCustomerDetails() {
            paint.typeface = boldTypeface
            paint.color = blueColor
            paint.textSize = 17f
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("Customer Details", margin, currentY, paint)
            currentY += 20f

            paint.typeface = regularTypeface
            paint.color = Color.BLACK
            paint.textSize = 14f

            val customerDetails = "Name: ${quotation.customerName}"
            val dateDetails = "Quotation Date: ${quotation.dateGenerated}"
            canvas.drawText(customerDetails, margin, currentY, paint)
            canvas.drawText(dateDetails, 376f, currentY, paint)
            currentY += 18f

            val phoneNumber = "Phone: ${quotation.phoneNumber}"
            val validTill = "Valid Till: ${quotation.expiryDate}"
            canvas.drawText(phoneNumber, margin, currentY, paint)
            canvas.drawText(validTill, 420f, currentY, paint)
            currentY += 20f

            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, paint)
            currentY += 25f
        }

        fun drawTableHeader() {
            paint.typeface = boldTypeface
            paint.color = blueColor
            paint.textSize = 15f

            val headers = listOf("No.", "Camera Name", "Unit Price", "Qty", "Amount", "Tax", "Total Tax")
            val columnWidths = listOf(30f, 140f, 90f, 50f, 100f, 50f, 100f)
            var currentX = margin

            for ((index, header) in headers.withIndex()) {
                canvas.drawText(header, currentX, currentY, paint)
                currentX += columnWidths[index]
            }
            currentY += 25f
            paint.color = Color.BLACK
            canvas.drawLine(margin, currentY - 10f, pageWidth - margin, currentY - 10f, paint)
            currentY += 10f
        }

        drawHeader()
        drawCustomerDetails()
        drawTableHeader()

        paint.typeface = regularTypeface
        paint.textSize = 14f
        paint.color = Color.BLACK

        quotation.camera.forEachIndexed { index, camera ->
            if (currentY + 50f > maxContentHeight) {
                pdfDocument.finishPage(page)
                page = createNewPage()
                canvas.drawText("Continued...", margin, 40f, paint)
                currentY = 60f
                drawHeader()
                drawTableHeader()
            }

            val quantity = quotation.quantity[index]
            val amount = quantity * camera.unitPrice
            val tax = (camera.gst * amount) / 100
            totalAmount += amount + tax

            val rowData = listOf(
                "${index + 1})",
                camera.name,
                "₹${formatNumberIntoIndianNumber(camera.unitPrice)}",
                "$quantity",
                "₹${formatNumberIntoIndianNumber(amount)}",
                "${camera.gst}%",
                "₹${formatNumberIntoIndianNumber(tax)}"
            )

            val columnWidths = listOf(30f, 140f, 90f, 50f, 100f, 70f, 100f)
            var currentX = margin
            for ((colIndex, cellData) in rowData.withIndex()) {
                if (colIndex == 1) {
                    currentX += columnWidths[colIndex]
                    continue // Skip direct text drawing for camera name
                }
                canvas.drawText(cellData, currentX, currentY, paint)
                currentX += columnWidths[colIndex]
            }

            // Draw Camera Name as multi-line
            val cameraNameX = margin + columnWidths[0]
            var cameraNameY = currentY

            val wrappedName = wrapTextToLines(
                text = camera.name,
                width = columnWidths[1].toInt() - 10,
                paint = paint
            ) // Wrap text

            val rowHeight = (wrappedName.size * 22f).coerceAtLeast(25f) // Dynamic row height based on text lines

            for (line in wrappedName) {
                canvas.drawText(line, cameraNameX, cameraNameY, paint)
                cameraNameY += 18f // Line spacing
            }

            currentY += rowHeight // Move to the next row

        }

        fun drawFooter() {
            paint.textSize = 16f
            if (currentY + 100f > maxContentHeight) {
                pdfDocument.finishPage(page)
                page = createNewPage()
                currentY = 60f
                drawHeader()
            }
            currentY += 20f

            paint.typeface = boldTypeface
            paint.color = blueColor
            canvas.drawText("TOTAL:", 430f, currentY, paint)
            canvas.drawText("₹${formatNumberIntoIndianNumber(totalAmount)}", 490f, currentY, paint)
            currentY += 40f

            paint.typeface = regularTypeface
            paint.color = Color.BLACK

            if (quotation.extraDiscount) {
                canvas.drawText("Discount: ₹${formatNumberIntoIndianNumber(quotation.discountAmount)}", margin, currentY, paint)
                currentY += 30f
            }
            if (quotation.prepaymentAmount != 0.0) {
                canvas.drawText("Advance: ₹${formatNumberIntoIndianNumber(quotation.prepaymentAmount)}", margin, currentY, paint)
                currentY += 30f
                totalAmount -= quotation.discountAmount
            }

            paint.textSize = 17f
            canvas.drawText("Total Amount: ₹${formatNumberIntoIndianNumber(totalAmount)}", margin, currentY, paint)
            currentY += 30f

            paint.textSize = 19f
            paint.typeface = boldTypeface
            paint.color = redColor
            totalAmount -= quotation.prepaymentAmount
            canvas.drawText("Amount to be Paid: ₹${formatNumberIntoIndianNumber(totalAmount)}", margin, currentY, paint)
            currentY += 30f

            paint.textSize = 17f
            paint.color = Color.BLACK
            canvas.drawText("Amount in Words: ${convertNumberToWords(totalAmount)}", margin, currentY, paint)
        }

        drawFooter()
        pdfDocument.finishPage(page)

        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val file = File(directoryPath, "${quotation.id} invoice.pdf")

        FileOutputStream(file).use { fos -> pdfDocument.writeTo(fos) }
        openPdfFile(context, file)
        pdfDocument.close()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("Exception", e.toString())
    }
}


fun openPdfFile(context: Context, file: File) {
    if (!file.exists()) {
        Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show()
        return
    }
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    context.startActivity(intent)
}

