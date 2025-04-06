package org.project.we3.ui.screens.admin.quotation

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
import org.project.we3.data.model.Camera
import org.project.we3.data.model.Quotation
import org.project.we3.domain.usecase.GeneratePDFUseCase
import org.project.we3.domain.usecase.earning.RemoveEarningUseCase
import org.project.we3.domain.usecase.earning.UpdateEarningUseCase
import org.project.we3.domain.usecase.quotation.RemoveQuotationUseCase
import org.project.we3.domain.usecase.quotation.ToggleActiveStatusUseCase
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class QuotationDetailViewModel @Inject constructor(
    private val removeQuotationUseCase: RemoveQuotationUseCase,
    private val toggleActiveStatusUseCase: ToggleActiveStatusUseCase,
    private val updateEarningUseCase: UpdateEarningUseCase,
    private val removeEarningUseCase: RemoveEarningUseCase,
    private val generatePDFUseCase: GeneratePDFUseCase
) : ViewModel() {
    private val _quotation = MutableStateFlow<Quotation>(Quotation())
    val quotation: StateFlow<Quotation> = _quotation
    private var cameraList: StateFlow<List<Camera>> = MutableStateFlow<List<Camera>>(emptyList())
    var showDeleteDialog by mutableStateOf(false)  // State to show/hide dialog
    var showCompletedDialog by mutableStateOf(false) // State to show/hide dialog
    var showLoader by mutableStateOf(false) // State to show/hide loader

    fun setQuotationAndCamera(quotation: Quotation, camera: StateFlow<List<Camera>>) {
        viewModelScope.launch {
            _quotation.emit(quotation)
            cameraList = camera
        }
    }

    fun navigateToEditScreen(quotation: Quotation) {
        Router.navigateTo(Screen.QuotationEditScreen(quotation))
    }

    fun removeQuotation(){
        viewModelScope.launch(Dispatchers.IO) {
            removeQuotationUseCase.invoke(quotation.value)
            withContext (Dispatchers.IO){
                removeEarningUseCase.invoke(quotation.value.firestoreId)
                showLoader = false
                Router.navigateTo(Screen.AdminSectionNavigatorScreen)
            }
        }

    }

    fun toggleActiveStatus(){
        viewModelScope.launch(Dispatchers.IO){
            toggleActiveStatusUseCase.invoke(
                quotation.value.firestoreId,
                !quotation.value.isActive
            )
            withContext(Dispatchers.IO) {
                updateEarningUseCase.invoke(quotation.value, cameraList.value)
                showLoader = false
                Router.navigateTo(Screen.AdminSectionNavigatorScreen)
            }
        }
    }

    fun showPDF(context: Context, quotation: Quotation) {
        viewModelScope.launch (Dispatchers.IO) {
            generatePDFUseCase.invoke(context, quotation)
            withContext (Dispatchers.IO){
                showLoader = false
            }
        }
    }
}