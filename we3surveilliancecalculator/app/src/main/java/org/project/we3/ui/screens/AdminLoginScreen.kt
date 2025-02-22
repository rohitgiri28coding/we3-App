package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

@Composable
fun AdminLoginScreen(onLoginSuccess: () -> Unit, adminLoginViewModel: AdminLoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (adminLoginViewModel.isAdminLoggedIn) {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Admin Login", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(30.dp))

        email = credentialEditSection("Email", email, ImeAction.Next)

        Spacer(modifier = Modifier.height(8.dp))

        password = credentialEditSection("Password", password, ImeAction.Done)


        Spacer(modifier = Modifier.height(20.dp))

        errorMessage?.let {
            Text(text = it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (!adminLoginViewModel.login(email, password)){
                    errorMessage = "Invalid email or password"
                }else{
                    onLoginSuccess()
                }
            }
        ) {
            Text("Login", fontSize = 20.sp)
        }
    }
}

@Composable
private fun editSection(textValue: String, value: String, imeAction: ImeAction): String {
    var v1 by remember {  mutableStateOf(value)}
    TextField(
        value = v1,
        onValueChange = { v1 = it },
        label = { Text(textValue, color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
        keyboardOptions = KeyboardOptions(imeAction = imeAction)
    )
    return v1
}

@Composable
private fun credentialEditSection(textValue: String, value: String, imeAction: ImeAction): String {
    var v1 by remember {  mutableStateOf(value)}
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )

    ) {
        v1 = editSection(textValue, v1, imeAction)
    }
    return v1
}
