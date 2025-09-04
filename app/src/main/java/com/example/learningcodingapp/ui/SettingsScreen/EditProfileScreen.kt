package com.example.learningcodingapp.ui.SettingsScreen

import com.example.learningcodingapp.data.UserProfile.ProfileViewModel

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val vm: ProfileViewModel = viewModel()
    val ui = vm.state.collectAsState().value

    // حالتا الاسم والصورة محلياً للواجهة
    var name by remember(ui.profile?.displayName) { mutableStateOf(ui.profile?.displayName ?: "") }
    var pickedImage by remember { mutableStateOf<Uri?>(null) }

    // لانتقاء صورة من المعرض (بدون صلاحيات على أغلب الإصدارات)
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) pickedImage = uri }

    val snack = remember { SnackbarHostState() }

    // لو صار خطأ في الـ VM اعرضه
    LaunchedEffect(ui.error) {
        ui.error?.let { snack.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        enabled = !ui.saving && name.isNotBlank(),
                        onClick = {
                            // احفظ الاسم
                            if (name.isNotBlank()) vm.updateName(name)
                            // وارفع الصورة لو تم اختيارها
//                            pickedImage?.let { vm.updateAvatar(it) }
                        }
                    ) { Text("Save") }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snack) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // الصورة الرمزية
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                    .clickable { pickImage.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                val model: Any? = pickedImage ?: ui.profile?.photoUrl
                if (model != null) {
                    AsyncImage(
                        model = model,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = (ui.profile?.displayName?.firstOrNull() ?: 'A').uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = ui.profile?.email ?: "",
                onValueChange = {},
                enabled = false,
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (ui.loading || ui.saving) {
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Tip: tap the avatar to choose a new photo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
