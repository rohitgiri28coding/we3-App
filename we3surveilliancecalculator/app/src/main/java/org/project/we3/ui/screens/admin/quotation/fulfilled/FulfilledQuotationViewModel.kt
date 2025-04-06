package org.project.we3.ui.screens.admin.quotation.fulfilled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.project.we3.data.model.Quotation
import org.project.we3.domain.usecase.quotation.FetchQuotationUseCase
import javax.inject.Inject

@HiltViewModel
class FulfilledQuotationViewModel @Inject constructor(private val fetchQuotationUseCase: FetchQuotationUseCase) : ViewModel(){
    private val _fulfilledQuotations = MutableStateFlow<List<Quotation>>(emptyList())
    val fulfilledQuotations: StateFlow<List<Quotation>> = _fulfilledQuotations

    init {
        fetchFulfilledQuotations()
    }
    fun fetchFulfilledQuotations() {
        viewModelScope.launch(Dispatchers.IO){
            _fulfilledQuotations.emit(fetchQuotationUseCase.invoke(false))
        }
    }
}