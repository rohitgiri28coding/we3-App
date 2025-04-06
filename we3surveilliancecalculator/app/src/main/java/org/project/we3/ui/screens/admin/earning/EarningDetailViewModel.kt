package org.project.we3.ui.screens.admin.earning

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.we3.data.model.Quotation
import org.project.we3.domain.usecase.GeneratePDFUseCase
import org.project.we3.domain.usecase.quotation.FetchSpecificQuotationUseCase
import javax.inject.Inject

@HiltViewModel
class EarningDetailViewModel @Inject constructor(
    private val fetchSpecificQuotationUseCase: FetchSpecificQuotationUseCase,
    private val generatePDFUseCase: GeneratePDFUseCase
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    private val _quotations = MutableStateFlow<Quotation>(Quotation())
    val quotations: StateFlow<Quotation> = _quotations

    fun fetchQuotation(quotationId: String) {
        viewModelScope.launch (Dispatchers.IO) {
            _quotations.emit(fetchSpecificQuotationUseCase.invoke(quotationId))
        }
    }

    fun showPDF(context: Context, quotation: Quotation) {
        viewModelScope.launch (Dispatchers.IO) {
            generatePDFUseCase.invoke(context, quotation)
            withContext (Dispatchers.IO){
                isLoading = false
            }
        }
    }
}