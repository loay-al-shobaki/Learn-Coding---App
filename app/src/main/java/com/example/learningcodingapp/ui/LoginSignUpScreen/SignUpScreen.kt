

@file:OptIn(ExperimentalMaterial3Api::class)



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learningcodingapp.R
import com.example.learningcodingapp.core.di.AuthViewModel
import kotlinx.coroutines.launch


// Palette & Dimens
private val PageBg = Color(0xFFF5F6FA)
private val Primary = Color(0xFF3F4EB3)
private val Blob = Color(0xFFE7EBFF)
private val FieldBg = Color.White

private object DimensSU {
    val ContentMaxWidth = 320.dp
    val ScreenHPad = 20.dp
    val TopSpacer = 28.dp
    val LogoSize = 84.dp
    val TitleTop = 14.dp
    val FieldsTop = 16.dp
    val FieldGap = 10.dp
    val FieldRadius = 16.dp
    val ButtonH = 52.dp
    val SignUpTop = 18.dp
    val AlreadyTop = 14.dp
    val LoginTop = 10.dp
    val BottomSpacer = 36.dp
    val IconBox = 40.dp
}

@Composable
fun SignUpScreen(
    onBack: () -> Unit,              // مثلاً رجوع للـ Welcome
    onLoginClick: () -> Unit = onBack, // يفتح شاشة تسجيل الدخول
    onSignedUp: () -> Unit = onBack,   // ماذا تفعل بعد النجاح (لاحقًا تروح HOME)
    vm: AuthViewModel = hiltViewModel()
) {
    val state = vm.state
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current

    var user by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var passVisible by rememberSaveable { mutableStateOf(false) }
    var confirmVisible by rememberSaveable { mutableStateOf(false) }

    fun isEmailValid(s: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
    val passOk = pass.length >= 6
    val confirmOk = confirm == pass && confirm.isNotEmpty()
    val emailOk = isEmailValid(email)
    val formOk = emailOk && passOk && confirmOk

    // لو نجح التسجيل انتقل
    LaunchedEffect(state.success) {
        if (state.success) {
            onSignedUp()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)

    ) {
        BackgroundBlobs_SignUp()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = DimensSU.ScreenHPad, vertical = 0.dp) // هنا البادينق
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(DimensSU.TopSpacer))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = DimensSU.ContentMaxWidth),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_coding_app),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(DimensSU.LogoSize)
                        .clip(CircleShape)
                        .border(2.dp, Primary.copy(alpha = 0.35f), CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(DimensSU.TitleTop))

                Text(
                    text = "sign up",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF121212)
                )

                Spacer(Modifier.height(DimensSU.FieldsTop))

                // User name
                SUField(
                    value = user,
                    onValueChange = { user = it },
                    placeholder = "User name",
                    leading = { IconBadgeSU { Icon(Icons.Filled.Person, null, tint = Color.White) } }
                )

                Spacer(Modifier.height(DimensSU.FieldGap))

                // Email
                SUField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email address",
                    leading = { IconBadgeSU { Icon(Icons.Filled.Email, null, tint = Color.White) } }
                )
                if (email.isNotBlank() && !emailOk)
                    Text("Enter a valid email", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)



                Spacer(Modifier.height(DimensSU.FieldGap))

                // Password
                SUField(
                    value = pass,
                    onValueChange = { pass = it },
                    placeholder = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    leading = { IconBadgeSU { Icon(Icons.Filled.Lock, null, tint = Color.White) } }
                )

                Spacer(Modifier.height(DimensSU.FieldGap))

                // Confirm password
                SUField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    placeholder = "Confirm password",
                    visualTransformation = PasswordVisualTransformation(),
                    leading = { IconBadgeSU { Icon(Icons.Filled.Lock, null, tint = Color.White) } }
                )

//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(checked = confirmVisible, onCheckedChange = { confirmVisible = it })
//                    Text("Show confirm", fontSize = 12.sp)
//                }
//                if (confirm.isNotBlank() && !confirmOk)
//                    Text("Passwords do not match", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)

                // Sign up (primary)
                Spacer(Modifier.height(DimensSU.SignUpTop))
                Button(
                    onClick = {
                        focus.clearFocus()
                        if (!formOk) {
                            scope.launch {
                                snack.showSnackbar("Check your inputs: valid email, 6+ chars, matching passwords")
                            }
                        } else {
                            vm.signUp(email, pass)
                        }
                    },
                    enabled = !state.loading,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensSU.ButtonH),
                    shape = RoundedCornerShape(DimensSU.FieldRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White
                    )
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Sign up", fontSize = 16.sp)
                    }
                }

                // رسالة خطأ من الـ ViewModel
                state.error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                // Already have account ?
                Spacer(Modifier.height(DimensSU.AlreadyTop))
                Text("Already have account ?", fontWeight = FontWeight.SemiBold)
                // Log in (outlined, arrow left)
                Spacer(Modifier.height(DimensSU.LoginTop))
                OutlinedButton(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensSU.ButtonH),
                    shape = RoundedCornerShape(DimensSU.FieldRadius),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(Primary)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
                ) {
                    Spacer(Modifier.width(6.dp))
                    Text("Log in")
                }

                Spacer(Modifier.height(DimensSU.BottomSpacer))
            }
        }
        // Snackbar للأخطاء السريعة
        SnackbarHost(
            hostState = snack,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
@Composable
private fun SUField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None,
    leading: @Composable () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        leadingIcon = leading,
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DimensSU.FieldRadius),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
private fun IconBadgeSU(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(DimensSU.IconBox)
            .clip(RoundedCornerShape(10.dp))
            .background(Primary),
        contentAlignment = Alignment.Center
    ) { content() }
}

@Composable
private fun BackgroundBlobs_SignUp() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Blob كبير أعلى اليسار (أقرب للعنوان)
        val p1 = Path().apply {
            moveTo(0f, h * 0.17f)
            cubicTo(w * 0.18f, h * 0.05f, w * 0.46f, h * 0.12f, w * 0.52f, h * 0.26f)
            lineTo(0f, h * 0.26f)
            close()
        }
        drawPath(p1, Blob.copy(alpha = 0.85f))

        // Blob سفلي يمين
        val p2 = Path().apply {
            moveTo(w * 0.62f, h * 0.76f)
            cubicTo(w * 0.84f, h * 0.66f, w * 1.06f, h * 0.90f, w, h)
            lineTo(w * 0.60f, h)
            close()
        }
        drawPath(p2, Blob.copy(alpha = 0.90f))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_SignUpScreen() {
   // MaterialTheme { SignUpScreen() }
}
