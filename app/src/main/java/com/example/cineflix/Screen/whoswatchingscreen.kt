package com.example.cineflix.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfileScreen(navController: NavHostController) {
    var profileName by remember { mutableStateOf("") }
    var isChildrenProfile by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF121212), // Dark background
        topBar = {
            TopAppBar(
                title = { Text("Add Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { /* Save logic */ },
                        enabled = profileName.isNotBlank()
                    ) {
                        Text("Save", color = if (profileName.isNotBlank()) Color.White else Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFCC00)),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text("😊", fontSize = 40.sp, modifier = Modifier.align(Alignment.Center))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                        }
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = profileName,
                onValueChange = { profileName = it },
                label = { Text("Profile name", color = Color.LightGray, fontSize = 12.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF2C2C2C),
                    focusedContainerColor = Color(0xFF3C3C3C),
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp) // Reduced height
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Children's Profile",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Made for children 12 and under, but parents have all the control.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(modifier = Modifier.padding(start = 16.dp)) {
                    Switch(
                        checked = isChildrenProfile,
                        onCheckedChange = { isChildrenProfile = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.LightGray
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AddProfileScreenPreview() {
    AddProfileScreen(navController = rememberNavController())
}
@Preview(showSystemUi = true)
@Composable
fun PreviewAddProfileScreen() {
    AddProfileScreenPreview()
}