package org.project.we3.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.project.we3.app.Earning

class EarningSectionViewModel: ViewModel() {
    private val db = Firebase.firestore
    private val _earningList = MutableStateFlow<List<Earning>>(emptyList())
    val earningList: StateFlow<List<Earning>> = _earningList

    init {
        fetchEarningList()
    }
    fun fetchEarningList(){
        viewModelScope.launch {
            val doc = db.collection("earnings").get().await()
            val earnings = doc.documents.map { doc->
                Earning(
                    id = doc.getString("id") ?: "",
                    firestoreId = doc.id,
                    date = doc.getString("date") ?: "",
                    totalEarning = doc.getDouble("totalEarning") ?: 0.0,
                    prepayment = doc.getDouble("prepayment") ?: 0.0,
                    quotationId = doc.getString("quotationId") ?: ""
                )
            }
            _earningList.emit(earnings)
            Log.d("EarningSectionViewModel", "Earning List: $earnings")
        }
    }
}