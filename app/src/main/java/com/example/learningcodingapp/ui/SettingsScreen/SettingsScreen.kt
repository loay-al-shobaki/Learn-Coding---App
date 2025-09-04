package com.example.learningcodingapp.ui.SettingsScreen

import androidx.compose.material3.Text

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.learningcodingapp.data.UserProfile.ProfileViewModel

/**
 * UI فقط بدون منطق — جاهزة للاستخدام داخل MainScreen().
 * يمكن لاحقًا توصيل onClick/onToggle من الـ ViewModel حسب حاجتك.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onEditProfile: () -> Unit
) {


    // حالة UI محلية فقط لعرض السويتشات
    var pushEnabled by rememberSaveable { mutableStateOf(true) }
    var darkModeEnabled by rememberSaveable { mutableStateOf(false) }

    val topBarColor = MaterialTheme.colorScheme.primary
    val headerHeight = 96.dp
    val contentPadding = 16.dp

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Settings",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    // زر الرجوع (اختياري): خصّص سلوكه من الخارج لاحقًا لو حبيت
                    IconButton(onClick = { /* TODO: onBack() */ }) {}
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = topBarColor,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // خلفية هيدر ملوّنة مثل الصورة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(topBarColor)
            )

            // المحتوى الأبيض فوق الخلفية
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = contentPadding, vertical = 12.dp)
            ) {
                ProfileCardVM(onEditClick = { onEditProfile() })

                Spacer(Modifier.height(12.dp))

                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.elevatedCardColors()
                ) {
                    SectionLabel("Account Settings")

                    SettingsChevronItem(
                        title = "Edit profile",
                        onClick = onEditProfile
                    )
                    HorizontalDivider()
                    SettingsChevronItem(
                        title = "Change password",
                        onClick = onEditProfile
                    )
                    HorizontalDivider()
                    SettingsBadgeItem(
                        title = "Notifications",
                        badgeText = "!",
                        onClick = { /* TODO: onNotifications() */ }
                    )
                    HorizontalDivider()
                    SettingsSwitchItem(
                        title = "Push notifications",
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                    HorizontalDivider()
                    SettingsSwitchItem(
                        title = "Dark mode",
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it },
                        enabled = false // معطّل مثل اللي بالصورة
                    )
                }

                Spacer(Modifier.height(16.dp))

                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    SectionLabel("More")

                    SettingsChevronItem(
                        title = "About us",
                        onClick = { /* TODO: onAbout() */ }
                    )
                    HorizontalDivider()
                    SettingsChevronItem(
                        title = "Privacy policy",
                        onClick = { /* TODO: onPrivacy() */ }
                    )
                    HorizontalDivider()
                    SettingsChevronItem(
                        title = "Terms and conditions",
                        onClick = { /* TODO: onTerms() */ }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/* ----------------------- عناصر UI صغيرة قابلة لإعادة الاستخدام ----------------------- */

@Composable
private fun ProfileCardVM(
    onEditClick: () -> Unit
) {
    val vm: ProfileViewModel = viewModel()
    val ui = vm.state.collectAsState().value
    // لو لأي سبب الواجهة انبنت قبل ما ينطلق refresh()، فعّل تأكيد
    LaunchedEffect(Unit) {
        if (ui.profile == null && !ui.loading && ui.error == null) vm.refresh()
    }

    val displayName = ui.profile?.displayName?.ifBlank { null }
        ?: ui.profile?.email?.substringBefore("@")
        ?: "User"

//    Text(
//        text = if (ui.loading) "Loading..." else displayName,
//        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
//    )

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // صورة رمزية — استبدلها بصورة شبكة/مورد عندك
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (ui.profile?.photoUrl != null) {
                    AsyncImage(
                        model = ui.profile.photoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = displayName.first().uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = if (ui.loading) "Loading..." else displayName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = ui.profile?.email.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onEditClick, enabled = ui.profile != null) {
                Text("Edit")
            }
        }
    }
    // لو فيه خطأ اظهره تحت البطاقة (اختياري)
    if (ui.error != null) {
        Spacer(Modifier.height(8.dp))
        Text(ui.error!!, color = MaterialTheme.colorScheme.error)
    }
}
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 6.dp),
        style = MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun SettingsChevronItem(
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
private fun SettingsBadgeItem(
    title: String,
    badgeText: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) { Text(badgeText) }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
                enabled = enabled
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}
