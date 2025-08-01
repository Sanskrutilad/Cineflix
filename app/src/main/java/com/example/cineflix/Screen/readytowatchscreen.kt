package com.example.cineflix.Screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineflix.Viewmodel.LoginScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadyToWatchScreen(
    navController: NavController,
    loginViewModel: LoginScreenViewModel = viewModel()
) {
    val emailState = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        IconButton(
            onClick = {navController.popBackStack()},
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "Ready to watch?",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your email to create or sign in to your account.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color(0xFF00C853)),
                        shape = RoundedCornerShape(6.dp)
                    ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                shape = RoundedCornerShape(6.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    loginViewModel.isUserRegistered(emailState.value) { isRegistered ->
                        Log.d("FB", "isUserRegistered callback for $emailState.value: $isRegistered")
                        if (isRegistered) {
                            navController.navigate("loginscreen")
                        } else {
                            navController.navigate("signupscreen")
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("GET STARTED", fontWeight = FontWeight.Bold)
            }
        }
    }
}

