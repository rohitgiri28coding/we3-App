package org.project.we3.domain.usecase

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.project.we3.app.createQuotationPDF
import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.CameraRepository
import org.project.we3.data.repository.QuotationRepository
import java.io.File

class GeneratePDFUseCase(
    private val quotationRepository: QuotationRepository,
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(context: Context, quotation: Quotation){
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val file = File(directoryPath, "${quotation.id} invoice.pdf")
        try {
            if (!file.exists()) {
                val quotation = retrieveQuotation(quotation.firestoreId)
                withContext (Dispatchers.IO){
                    createQuotationPDF(context, quotation)
                }
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private suspend fun retrieveQuotation(quotationId: String): Quotation {
        val quotation = quotationRepository.fetchSpecificQuotation(quotationId)
        val cameras = cameraRepository.fetchCamera()
        val filteredCameras = cameras.filter { camera ->
            quotation.camera.any { it.firestoreId == camera.firestoreId }
        }
        quotation.camera = filteredCameras
        return quotation
    }
}