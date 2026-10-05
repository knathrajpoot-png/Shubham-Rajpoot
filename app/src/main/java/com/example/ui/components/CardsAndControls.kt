package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BottomTab

@Composable
fun NeonGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = FitCardBorderNeon.copy(alpha = 0.45f),
    backgroundColor: Color = FitCardBackground,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .shadow(elevation = 12.dp, shape = shape, spotColor = NeonPurple.copy(alpha = 0.25f))
        .clip(shape)
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    backgroundColor.copy(alpha = 0.95f),
                    Color(0xFF090D18).copy(alpha = 0.95f)
                )
            )
        )
        .border(width = 1.2.dp, color = borderColor, shape = shape)

    val finalModifier = if (onClick != null) {
        baseModifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = NeonPurple),
            onClick = onClick
        )
    } else baseModifier

    Box(modifier = finalModifier) {
        content()
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    onTap: (() -> Unit)? = null
) {
    NeonGlassCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.35f),
        onClick = onTap
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (onTap != null) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = accentColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = unit,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "• $title",
                    color = TextMutedGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .shadow(10.dp, shape = shape, spotColor = color.copy(alpha = 0.3f))
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.22f),
                        color.copy(alpha = 0.08f)
                    )
                )
            )
            .border(1.2.dp, color.copy(alpha = 0.6f), shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = color),
                onClick = onClick
            )
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.25f))
                    .border(1.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.6.sp
            )
        }
    }
}

@Composable
fun CircularNeonProgress(
    progressPercent: Int, // 0..100
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    strokeWidth: Dp = 8.dp,
    accentColor: Color = NeonPurple,
    centerContent: @Composable () -> Unit
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)

            // Background Track
            drawArc(
                color = Color(0xFF192033),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Glowing Progress Arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        accentColor.copy(alpha = 0.6f),
                        accentColor,
                        NeonPurpleGlow
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress.value,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        centerContent()
    }
}

@Composable
fun GlowingGradientButton(
    text: String,
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA855F7), Color(0xFF00F0FF))),
    icon: ImageVector? = null,
    testTag: String = "primary_button",
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(12.dp, shape = shape, spotColor = NeonPurple.copy(alpha = 0.5f))
            .clip(shape)
            .background(gradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White),
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun TopAppBarHeader(
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onAvatarClick: () -> Unit,
    notificationCount: Int = 2
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Hamburger Menu
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F1524))
                .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
                .testTag("menu_button")
        ) {
            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = "Menu",
                tint = TextWhite,
                modifier = Modifier.size(22.dp)
            )
        }

        // App Brand Title
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "THEFIT SHUBHAM",
                color = TextWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.8.sp
            )
            Text(
                text = "AI WORKOUT & FITNESS COACH",
                color = NeonCyan,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        // Right Action: Notification Bell & User Avatar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F1524))
                    .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
                    .clickable { onNotificationClick() }
                    .testTag("notification_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TextWhite,
                    modifier = Modifier.size(20.dp)
                )
                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonPurpleGlow)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // User / Coach Avatar
            CoachCharacterImage(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onAvatarClick() }
                    .testTag("avatar_button"),
                shape = CircleShape,
                borderWidth = 1.5.dp,
                allowPicker = false
            )
        }
    }
}

@Composable
fun AppBottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    val tabs = listOf(
        Triple(BottomTab.HOME, "HOME", Icons.Default.FitnessCenter),
        Triple(BottomTab.WORKOUT, "WORKOUT", Icons.Default.FitnessCenter),
        Triple(BottomTab.DIET, "DIET", Icons.Default.Restaurant),
        Triple(BottomTab.ACTIVITY, "ACTIVITY", Icons.Default.LocalFireDepartment),
        Triple(BottomTab.PROFILE, "PROFILE", Icons.Default.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color(0xFF070B14))
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF1E1738),
                        NeonPurple.copy(alpha = 0.5f),
                        Color(0xFF0B2538)
                    )
                ),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (tab, label, icon) ->
                val isSelected = selectedTab == tab
                val tint = if (isSelected) NeonPurpleGlow else TextDarkGray

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("tab_${label.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = tint,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = label,
                        color = tint,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        letterSpacing = 0.6.sp
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 16.dp, height = 2.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(NeonPurpleGlow)
                        )
                    }
                }
            }
        }
    }
}
