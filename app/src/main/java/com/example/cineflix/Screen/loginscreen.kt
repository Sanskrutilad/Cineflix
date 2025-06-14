package com.example.cineflix.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflix.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixLoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        NetflixTopAppBar()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                placeholder = { Text("Email or phone number") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.DarkGray,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedPlaceholderColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.DarkGray,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedPlaceholderColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* handle sign in */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.White),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Sign In", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Need help?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "New to Netflix? Sign up now.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sign in is protected by Google\nreCAPTCHA to ensure you’re not a bot.\nLearn more.",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixTopAppBar() {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.netflixlogo_no_bg), // replace with your actual logo resource
                contentDescription = "Netflix Logo",
                modifier = Modifier
                    .height(36.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* handle back navigation */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NetflixLoginScreenPreview() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            NetflixLoginScreen()
        }
}
