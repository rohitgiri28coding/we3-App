package org.project.we3.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.project.we3.app.db.Quotation

@Composable
fun ActiveQuotationListScreen(quotations: List<Quotation>){

    LazyColumn {
        itemsIndexed(quotations){ index, quotation ->
            QuotationListComponent(quotation.camera[index].name+" ("+quotation.quantity[index])
        }
    }
}


@Composable
fun QuotationListComponent(data: String){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = data, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
    }
}