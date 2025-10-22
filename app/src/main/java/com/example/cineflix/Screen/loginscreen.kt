package com.example.cineflix.Screen

import android.R.attr.password
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cineflix.R
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Viewmodel.LoginScreenViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixLoginScreen(navController: NavHostController,  loginViewModel: LoginScreenViewModel = viewModel()) {
    Scaffold(
        topBar = { NetflixTopAppBar() },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            Spacer(modifier = Modifier.height(48.dp))

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

            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password", tint = Color.White)
                    }
                },
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
                onClick = {
                    Log.d("LoginButton", "Sign In button clicked")
                    Log.d("LoginButton", "Email: ${emailState.value}")
                    Log.d("LoginButton", "Password: ${passwordState.value}")
                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail("s@gmail.com")
                        .addOnSuccessListener {
                            Log.d("SIGN_METHODS", "Sign-in methods: ${it.signInMethods}")
                        }

                    loginViewModel.signInWithEmailAndPassword(
                        email = emailState.value,
                        password = passwordState.value,
                        home = {
                            Log.d("LoginButton", "Navigation to HomeScreen triggered")
                            navController.navigate("WhosWatchingScreen")
                        }
                    )
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d("USER", "UID: ${user?.uid}, email: ${user?.email}, provider: ${user?.providerId}")

                },
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
                fontSize = 14.sp,
                modifier=Modifier.clickable{navController.navigate("Helpscreen")}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "New to Netflix? Sign up now.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier=Modifier.clickable{navController.navigate("Signupscreen")}

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
                painter = painterResource(id = R.drawable.netflixlogo), // replace with your actual logo resource
                contentDescription = "Netflix Logo",
                modifier = Modifier
                    .height(90.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = {  }) {
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

