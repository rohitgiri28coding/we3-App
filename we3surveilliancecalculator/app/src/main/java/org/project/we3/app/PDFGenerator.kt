package org.project.we3.app

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.project.we3.ui.theme.AppTypography
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
fun createQuotationPDF(context: Context, cameraList: List<Camera>, quantity: Int) {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val margin = 20f
    var currentY = 60f
    var totalAmount = 0.0

    // Use custom fonts from the provided Typography
    val brandingTypeface = Typeface.create(AppTypography.displayLarge.fontFamily?.toString(), Typeface.NORMAL)
    val bodyTypeface = Typeface.create(AppTypography.bodyLarge.fontFamily?.toString(), Typeface.NORMAL)

    // Branding Header
    paint.typeface = brandingTypeface
    paint.textSize = 22f
    paint.isFakeBoldText = true
    paint.textAlign = Paint.Align.CENTER
    canvas.drawText("We3 Surveillance Camera Store", 297.5f, currentY, paint) // Centered on A4 width (595/2)
    currentY += 25f

    paint.textSize = 15f
    paint.isFakeBoldText = false
    canvas.drawText("Bakerganj, Patna", 297.5f, currentY, paint)
    currentY += 20f

    canvas.drawText("Phone No.: 9386673993", 297.5f, currentY, paint)
    currentY += 25f

    // Draw a separator line
    paint.strokeWidth = 1f
    canvas.drawLine(margin, currentY, 575f, currentY, paint)
    currentY += 30f // Adjusted spacing after separator

    // Section Title
    paint.typeface = brandingTypeface
    paint.textSize = 17f
    paint.isFakeBoldText = true
    paint.textAlign = Paint.Align.LEFT
    canvas.drawText("Quotation Details", 250f, currentY, paint)
    currentY += 40f
    paint.textAlign = Paint.Align.LEFT

    // Camera Details
    paint.typeface = bodyTypeface
    paint.textSize = 15f
    paint.isFakeBoldText = false
    cameraList.forEach { camera ->
        val cameraDetails = "${camera.name}: ${camera.detail}"
        val cameraLines = wrapTextToLines(text = cameraDetails, paint = paint)

        for (line in cameraLines) {
            canvas.drawText(line, margin, currentY, paint)
            currentY += 18f
        }
        currentY += 10f // Spacing after each camera
    }

    currentY += 20f // Adjusted spacing before table
    // Table Header
    paint.typeface = brandingTypeface
    paint.textSize = 15f
    paint.isFakeBoldText = true
    val headers = listOf("Unit Price", "Quantity", "Amount (GST Excluded)", "Tax Rate", "Total Tax")
    var columnWidths = listOf(100f, 80f, 170f, 100f, 120f)

    var currentX = margin
    for ((index, header) in headers.withIndex()) {
        canvas.drawText(header, currentX, currentY, paint)
        currentX += columnWidths[index]
    }
    currentY += 25f // Increased spacing between header and rows
    canvas.drawLine(margin, currentY - 10f, 575f, currentY - 10f, paint) // Line below header
    currentY += 10f
    // Table Rows
    paint.typeface = bodyTypeface
    paint.textSize = 12f
    paint.isFakeBoldText = false
    columnWidths = listOf(120f, 100f, 130f, 100f, 40f)
    cameraList.forEach { camera ->
        totalAmount = camera.gst * quantity * camera.unitPrice / 100 + quantity * camera.unitPrice

        val rowData = listOf(
            "₹${formatNumber(camera.unitPrice)}",
            "$quantity",
            "₹${formatNumber(quantity * camera.unitPrice)}",
            "${camera.gst}%",
            "₹${formatNumber(camera.gst * quantity * camera.unitPrice / 100)}"
        )

        currentX = margin+10f
        for ((index, cellData) in rowData.withIndex()) {
            canvas.drawText(cellData, currentX, currentY, paint)
            currentX += columnWidths[index]
        }
        currentY += 25f // Increased row spacing
    }
    currentY += 20f // Adjusted spacing before footer
    // Footer Totals
    paint.typeface = brandingTypeface
    paint.isFakeBoldText = true
    paint.textSize = 19f
    canvas.drawText("TOTAL:", 390f, currentY, paint)
    canvas.drawText("₹${formatNumber(totalAmount)}", 460f, currentY, paint)
    currentY += 40f // Adjusted spacing before amount in words

    // Amount in Words
    paint.textSize = 17f
    canvas.drawText("Amount in Words: ", margin, currentY, paint)
    paint.isFakeBoldText = false
    canvas.drawText(convertNumberToWords(totalAmount), margin + 140f, currentY, paint) // Aligned amount in words
    currentY += 30f

    pdfDocument.finishPage(page)

    // Save PDF to Downloads
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "Quotation_${cameraList[0].name}.pdf")
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    try {
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
                Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(openIntent)
            }
        } ?: throw IOException("Failed to create new MediaStore record.")
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
}
private fun formatNumber(number: Double): String {
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

