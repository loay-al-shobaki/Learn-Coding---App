
package com.example.learningcodingapp.ui.auth


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learningcodingapp.core.di.ResetPasswordViewModel
import com.example.learningcodingapp.ui.LoginSignUpScreen.blob


@Composable
fun ResetPasswordScreen(
    onBack: () -> Unit = {},
    onSignUp: () -> Unit = {},
    vm: ResetPasswordViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    val state = vm.state
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.sent) {
        if (state.sent) {
            snackbar.showSnackbar("If this email is registered, a reset link was sent.")
            onBack()
        }
    }
    // خطأ
    LaunchedEffect(state.error) {
        state.error?.let { snackbar.showSnackbar(it); vm.clearError() }
    }

    // ألوان قريبة من التصميم
    val bg = Color(0xFFF6F8FF)

    val blob = Color(0xFFDDE5FF)        // الأزرق الفاتح للخلفية
    val primary = Color(0xFF343F8F)

    androidx.compose.material3.Scaffold(
        snackbarHost = { androidx.compose.material3.SnackbarHost(snackbar) },
        containerColor = Color(0xFFF6F8FF)
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .background(bg)

        ) {
            // أشكال الخلفية
            BackgroundBlobs(color = blob)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Back
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B2B2B)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Title
                Text(
                    text = "Reset password",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1D1D),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(6.dp))

                // Description
                Text(
                    text = "We will email you\na link to reset your password.",
                    fontSize = 14.sp,
                    color = Color(0xFF6F6F6F),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                // Label
                Box(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        color = Color(0xFF3A3A3A),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
                Spacer(Modifier.height(6.dp))

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    placeholder = { Text("sarah.jansen@gmail.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { if (!state.loading) vm.send(email) }
                    )
                )
                Spacer(Modifier.height(16.dp))

                // Send button
                Button(
                    onClick = {  vm.send(email) },
                    enabled = !state.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primary)
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text("Send", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(26.dp))

                // Divider with OR
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF2F2F2F),
                        thickness = 1.dp
                    )
                    Text(
                        "  OR  ",
                        color = Color(0xFF2F2F2F),
                        fontSize = 12.sp
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF2F2F2F),
                        thickness = 1.dp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Sign up outlined button
                OutlinedButton(
                    onClick = onSignUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primary
                    )
                ) {
                    Text(
                        text = "sign up ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
@Composable
private fun BackgroundBlobs(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // شكل علوي يسار
        val p1 = Path().apply {
            moveTo(w * 0.0f, h * 0.0f)
            lineTo(w * 0.55f, 0f)
            quadraticBezierTo(w * 0.78f, h * 0.18f, w * 0.45f, h * 0.30f)
            quadraticBezierTo(w * 0.15f, h * 0.42f, 0f, h * 0.35f)
            close()
        }
        drawPath(p1, color)

        // Blob سفلي يمين
        val p2 = Path().apply {
            moveTo(w * 0.62f, h * 0.76f)
            cubicTo(w * 0.84f, h * 0.66f, w * 1.06f, h * 0.90f, w, h)
            lineTo(w * 0.60f, h)
            close()
        }
        drawPath(p2, blob.copy(alpha = 0.90f))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ResetPasswordPreview() {
    MaterialTheme {
        ResetPasswordScreen()
    }
}

