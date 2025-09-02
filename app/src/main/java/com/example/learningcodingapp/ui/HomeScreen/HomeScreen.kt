package com.example.learningcodingapp.ui.HomeScreen



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.learningcodingapp.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
private object T {
    val bg = Color(0xFFF6F8FF)           // خلفية عامة
    val surface = Color(0xFFFFFFFF)      // بطاقات بيضاء
    val primary = Color(0xFF343F8F)      // أزرق داكن
    val primary2 = Color(0xFF1D2F6F)     // أزرق أعمق للجراديانت
    val chipBlue = Color(0xFF4EA4FF)     // شارة النقاط
    val textMuted = Color(0xFF7C7C7C)
    val borderSoft = Color(0xFFE7ECF7)
    val tileBg = Color(0xFFF2F6FF)
}

@Composable
fun HomeScreen() {
    val navItems = listOf(
        NavItem("Home", Icons.Filled.Home),
        NavItem("Quizz", Icons.Filled.Article),
        NavItem("Rank", Icons.Filled.EmojiEvents),
        NavItem("Settings", Icons.Filled.Settings)
    )
    var selected by remember { mutableStateOf(0) }
    Scaffold(
        containerColor = T.bg,
        bottomBar = {
            BottomDock(navItems, selectedIndex = selected) { i -> selected = i }
        }
    ) { inner ->
        HomeContent(Modifier.padding(inner))
    }

}
@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    val userName = "Feras-ahmad"
    val userEmail = "email@any"
    val userPoints = 160

    Column(
        modifier
            .fillMaxSize()
            .background(T.bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {


        // Header card (avatar + name + points chip)
        HeaderRow(userName, userEmail, userPoints)

        Spacer(Modifier.height(14.dp))

        // Banner like Figma
        BannerCard(
            title = "Elevate Your Coding Skills\nand Challenge Yourself",
            subtitle = "Play fun quizzes to advance your\ncoding knowledge and grow\nevery day.",
            cta = "Play Now"
        )

        Spacer(Modifier.height(14.dp))

        // Search + filter row
        SearchWithFilter()

        Spacer(Modifier.height(10.dp))

        // Categories
        Text("Categories", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Spacer(Modifier.height(10.dp))
        CategoriesRow(
            listOf(
                Category("HTML", R.drawable.ic_html),
                Category("JAVASCRIPT", R.drawable.ic_javascript),
                Category("REACT", R.drawable.ic_react),
                Category("C++", R.drawable.ic_cplus),
                Category("PYTHON", R.drawable.ic_python)
            )
        )

        Spacer(Modifier.height(16.dp))

        // Recent Activity
        Text("Recent Activity", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Spacer(Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityCard(
                iconRes = R.drawable.ic_html, title = "HTML", subtitle = "30 Question",
                score = 26, total = 30, ringColor = Color(0xFFEF4444) // Red
            )
            ActivityCard(
                iconRes = R.drawable.ic_javascript, title = "JAVASCRIPT", subtitle = "30 Question",
                score = 20, total = 30, ringColor = Color(0xFFF59E0B) // Amber
            )
            ActivityCard(
                iconRes = R.drawable.ic_react, title = "REACT", subtitle = "30 Question",
                score = 25, total = 30, ringColor = Color(0xFF38BDF8) // Sky
            )
            ActivityCard(
                iconRes = R.drawable.ic_cplus, title = "C++", subtitle = "30 Question",
                score = 27, total = 30, ringColor = Color(0xFF1992D4) // Blue
            )
            ActivityCard(
                iconRes = R.drawable.ic_python, title = "PYTHON", subtitle = "30 Question",
                score = 22, total = 30, ringColor = Color(0xFF8B5CF6) // Violet
            )
        }

        Spacer(Modifier.height(80.dp)) // مساحة فوق الدوك
    }
}

/* =========================
   Header
   ========================= */
@Composable
private fun HeaderRow(name: String, email: String, points: Int) {
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo_coding_app),
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(2.dp, T.primary, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF1F2633))
            Text(email, fontSize = 12.sp, color = T.textMuted)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(T.chipBlue)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Star, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("$points", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

/* =========================
   Banner (gradient + ornaments)
   ========================= */
@Composable
private fun BannerCard(
    title: String = "Elevate Your Coding Skills\nand Challenge Yourself",
    subtitle: String = "Play fun quizzes to advance your\ncoding knowledge and grow\nevery day.",
    cta: String = "Play Now"
) {
    val radius = 16.dp
    val grad = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2E3E87),   // أغمق يسار
            Color(0xFF243577),
            Color(0xFF0F2C6A)    // أغمق يمين
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(radius))
            .background(grad)
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(radius)) // حد داخلي لطيف
    ) {
        // طبقة الزخارف ورسم الأشكال
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // شريط زجاجي عمودي يمين
            drawRoundRect(
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = .10f), Color.White.copy(alpha = .04f))
                ),
                topLeft = Offset(x = w * .50f, y = 0f),
                size = Size(width = w * .16f, height = h),
                cornerRadius = CornerRadius(18f, 18f),
                blendMode = BlendMode.SrcOver
            )

            // شبكة نقاط أعلى يمين
            val dot = Color.White.copy(alpha = .22f)
            val cell = 14f
            val startX = w * .78f
            val startY = h * .16f
            for (i in 0..3) for (j in 0..3) {
                drawCircle(
                    color = dot,
                    radius = 2.6f,
                    center = Offset(startX + i * cell, startY + j * cell)
                )
            }

            // علامة × وسط يمين
            val xC = Offset(w * .86f, h * .42f)
            drawLine(dot, xC + Offset(-8f, -8f), xC + Offset(8f, 8f), strokeWidth = 3f)
            drawLine(dot, xC + Offset(8f, -8f), xC + Offset(-8f, 8f), strokeWidth = 3f)

            // شيفرونات يسار أسفل
            fun drawChevron(x: Float, y: Float) {
                drawLine(dot, Offset(x, y), Offset(x + 10f, y + 10f), strokeWidth = 3f)
                drawLine(dot, Offset(x + 10f, y + 10f), Offset(x + 20f, y), strokeWidth = 3f)
            }
            val baseY = h * .78f
            drawChevron(w * .22f, baseY)
            drawChevron(w * .27f, baseY)
            drawChevron(w * .32f, baseY)

            // قوسان بأسفل اليمين
            drawCircle(
                color = Color.White.copy(alpha = .10f),
                radius = h * .70f,
                center = Offset(w * .78f, h * 1.05f)
            )
            drawCircle(
                color = Color.White.copy(alpha = .16f),
                radius = h * .48f,
                center = Offset(w * .78f, h * 1.05f)
            )
        }

        // النص والزر
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = Color(0xFFE0E7FF),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 6.dp // ظل خفيف مثل فيغما
            ) {
                Text(
                    text = cta,
                    color = Color(0xFF2E3E87),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        // الكأس الكبيرة يمين
        Image(
            painter = painterResource(id = R.drawable.img_5), // استبدل بموردك
            contentDescription = null,
            modifier = Modifier
                .size(120.dp) // أكبر حتى تتجاوز القوس
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp ,)
        )
    }
}

/* =========================
   Search + Filter
   ========================= */
@Composable
private fun SearchWithFilter() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )
        Spacer(Modifier.width(10.dp))
        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            tonalElevation = 2.dp,
            shadowElevation = 0.dp,
            onClick = {}
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filters", tint = Color(0xFF5A5A5A))
            }
        }
    }
}

/* =========================
   Categories Row
   ========================= */
data class Category(val title: String, val iconRes: Int)

@Composable
private fun CategoriesRow(items: List<Category>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(items.size) { i ->
            CategoryCard(items[i])
        }
    }
}

@Composable
private fun CategoryCard(item: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 86.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = T.tileBg,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = BorderStroke(1.dp, T.borderSoft)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 74.dp, height = 58.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(item.iconRes), contentDescription = item.title, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(item.title, fontSize = 12.sp, color = Color(0xFF3A3A3A), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/* =========================
   Recent Activity Card
   ========================= */
@Composable
private fun ActivityCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    score: Int,
    total: Int,
    ringColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp), clip = false), // ظل ناعم كتأثير فيغما
        shape = RoundedCornerShape(16.dp),
        color = T.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // left icon tile
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = T.tileBg,
                border = BorderStroke(1.dp, T.borderSoft)
            ) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Image(painterResource(iconRes), contentDescription = title, modifier = Modifier.size(22.dp))
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2B2B2B))
                Spacer(Modifier.height(2.dp))
                Text(subtitle, fontSize = 12.sp, color = T.textMuted)
            }

            ScoreBadge(score, total, ringColor)
        }
    }
}

@Composable
private fun ScoreBadge(score: Int, total: Int, ringColor: Color) {
    val pct = (score.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    val bgCircle = Color(0xFFF1F5FF)

    Box(
        modifier = Modifier
            .size(48.dp)
            .drawBehind {
                // outer soft background
                drawCircle(color = bgCircle)
                // progress ring
                val stroke = 5.dp.toPx()
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = 360f * pct,
                    useCenter = false,
                   style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            border = BorderStroke(1.dp, T.borderSoft)
        ) {
            Text(
                text = "${score}/${total}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 11.sp,
                color = Color(0xFF3A3A3A),
                maxLines = 1
            )
        }
    }
}

/* =========================
   Bottom Dock (rounded container)
   ========================= */
data class NavItem(val label: String, val icon: ImageVector)

@Composable
private fun BottomDock(items: List<NavItem>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            tonalElevation = 2.dp,
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, T.borderSoft),
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
        ) {}
        Row(
            Modifier
                .matchParentSize()
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { i, item ->
                val isSel = i == selectedIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelect(i) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        item.icon, contentDescription = item.label,
                        tint = if (isSel) T.primary else T.textMuted.copy(.9f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        item.label,
                        fontSize = 11.sp,
                        color = if (isSel) T.primary else T.textMuted
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun PreviewHome_ProUI() {
    HomeScreen()
}
