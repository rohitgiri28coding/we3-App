package org.project.we3.ui.screens.admin.quotation.active

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
class ActiveQuotationsViewModel @Inject constructor(
    private val fetchQuotationUseCase: FetchQuotationUseCase
): ViewModel() {
    private val _activeQuotations = MutableStateFlow<List<Quotation>>(emptyList())
    val activeQuotations: StateFlow<List<Quotation>> = _activeQuotations

    init {
        fetchActiveQuotations()
    }
    fun fetchActiveQuotations(){
        viewModelScope.launch (Dispatchers.IO){
            _activeQuotations.emit(fetchQuotationUseCase.invoke(true))
        }
    }
}