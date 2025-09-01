@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.learningcodingapp.ui.LoginSignUpScreen




import com.example.learningcodingapp.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learningcodingapp.core.di.AuthViewModel
import kotlinx.coroutines.launch

// Palette مطابقة للصورة
private val PageBg = Color(0xFFF5F6FA)
private val Primary = Color(0xFF3F4EB3)      // البنفسجي/أزرق الغامق
val blob = Color(0xFFDDE5FF)        // الأزرق الفاتح للخلفية
private val FieldBg = Color.White
private val FieldRadius = 14.dp

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onGotoSignUp: () -> Unit = {},
    onGotoForgot: () -> Unit = {},
    onLoggedIn: () -> Unit = {},
    vm: AuthViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var pwVisible by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // افترض أن عندك AuthUiState(success, error, loading)
    val ui = vm.state
    LaunchedEffect(Unit) { vm.checkIfSignedIn() }         // اختياري

    // تنقّل/أخطاء

    LaunchedEffect(ui.error) {
        ui.error?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
        }
    }
    // تحقق بسيط
    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    val emailValid = EMAIL_REGEX.matches(email.value.trim())
    val passwordValid = password.value.length >= 6
    val canSubmit = emailValid && passwordValid && !ui.loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)

    ) {
        // زخرفة الخلفية (blobs) مثل الصورة
        BackgroundBlobs(color = blob)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp , vertical = 96.dp)
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,


        ) {

            // Logo دائري مع حدود خفيفة
            Image(
                painter = painterResource(R.drawable.ic_logo_coding_app),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Primary.copy(alpha = 0.35f), CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Login",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF121212)
            )

            Spacer(Modifier.height(16.dp))

            // Email
            LabeledField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Email address",
                isError = email.value.isNotBlank() && !emailValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                leadingContent = {
                    IconBadge { Icon(Icons.Filled.Email, null, tint = Color.White) }
                }
            )
            if (email.value.isNotBlank() && !emailValid) {
                AssistiveText("Invalid email format")
            }
            // Password
            LabeledField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "Password",
                isError = password.value.isNotBlank() && !passwordValid,
                visualTransformation = if (pwVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingContent = {
                    IconButton(onClick = { pwVisible = !pwVisible }) {
                        Icon(
                            if (pwVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Toggle password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (canSubmit) vm.signIn(email.value, password.value) // اربط هنا
                    }
                ),
                leadingContent = {
                    IconBadge { Icon(Icons.Filled.Lock, null, tint = Color.White) }
                }
            )
            if (password.value.isNotBlank() && !passwordValid) {
                AssistiveText("At least 6 characters")
            }

            Spacer(Modifier.height(6.dp))

            // زر Log in (Primary)
            Button(
                onClick = {  vm.signIn(email.value.trim(), password.value) },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(FieldRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White,
                    disabledContainerColor = Primary.copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.8f)
                )
            ) {
                if (ui.loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Log in", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onGotoForgot) {
                Text("Forgot password ?", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0x1A000000))
                Text("  OR  ", color = Color.Gray)
                Divider(modifier = Modifier.weight(1f), color = Color(0x1A000000))
            }
            Spacer(Modifier.height(10.dp))

            // sign up (Outlined) مع سهم
            OutlinedButton(
                onClick =  onGotoSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(FieldRadius),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp,
                    brush = SolidColor(Primary.copy(alpha = 0.9f))
                ),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
            ) {
                Text("sign up")


            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LabeledField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        isError = isError,
        leadingIcon = leadingContent?.let { { IconBadge { it() } } },
        trailingIcon = trailingContent,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(FieldRadius),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
        )
    )
}

@Composable
private fun AssistiveText(text: String) {
    Text(
        text,
        color = Color(0xFFB00020),
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun IconBadge(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Primary),
        contentAlignment = Alignment.Center
    ) { content() }
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
@Preview(showBackground = true)
@Composable
private fun PreviewLoginScreen_ProUI() {
  //  MaterialTheme { LoginScreen() }
}
