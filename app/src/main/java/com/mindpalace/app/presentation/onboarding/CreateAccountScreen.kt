package com.mindpalace.app.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R
import com.mindpalace.app.presentation.components.AppTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(modifier: Modifier) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(R.drawable.register_image),
                    contentDescription = "Create Account Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 270.dp)
                        .padding(horizontal = 40.dp)
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.outline,
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = modifier.padding(25.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Create Account",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.height(10.dp)) // vertical space
                        Text(
                            "Enter details and begin your jot journey!",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(25.dp)) // vertical space

                        // email text field
                        AppTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp)) // vertical space

                        // password text field
                        AppTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = "Password",
                            modifier = Modifier.fillMaxWidth(),
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(12.dp)) // vertical space

                        // confirm password text field
                        AppTextField(
                            value = confirmPassword.value,
                            onValueChange = { confirmPassword.value = it },
                            label = "Confirm Password",
                            modifier = Modifier.fillMaxWidth(),
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(25.dp)) // vertical space

                        Button(
                            onClick = ({ }), colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Create Account", style = MaterialTheme.typography.labelLarge)
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        OutlinedButton(
                            onClick = ({}), colors = ButtonColors(
                                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                                contentColor = if (isSystemInDarkTheme()) Color(0xffFFFFFF) else Color(
                                    0xff3C4043
                                ),
                                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                1.dp,
                                if (isSystemInDarkTheme()) Color(0xFF5F6368) else Color(0xFFDADCE0)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google_icon_logo), // official "G" icon
                                contentDescription = "Google Sign-In",
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(end = 12.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = "Continue with Google",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                    }
                }
            }
        })
}
