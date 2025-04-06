package org.project.we3.ui.screens.admin.earning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.project.we3.data.model.Earning
import org.project.we3.domain.usecase.earning.FetchEarningUseCase
import javax.inject.Inject

@HiltViewModel
class EarningSectionViewModel @Inject constructor(
    private val fetchEarningUseCase: FetchEarningUseCase
) : ViewModel() {
    private val _earningList = MutableStateFlow<List<Earning>>(emptyList())
    val earningList: StateFlow<List<Earning>> = _earningList

    init {
        fetchEarningList()
    }
    fun fetchEarningList(){
        viewModelScope.launch (Dispatchers.IO){
            _earningList.emit(fetchEarningUseCase.invoke())
        }
    }
}