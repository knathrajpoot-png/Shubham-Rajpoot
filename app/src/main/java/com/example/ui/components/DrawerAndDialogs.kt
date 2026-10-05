package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiCoachInsight
import com.example.data.model.UserProfile
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BottomTab

@Composable
fun FitnessNavigationDrawerContent(
    userProfile: UserProfile?,
    currentTab: BottomTab,
    onSelectTab: (BottomTab) -> Unit,
    onOpenRunning: () -> Unit,
    onOpenMeditation: () -> Unit,
    onOpenReminders: () -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(Color(0xFF070B14))
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("nav_drawer")
    ) {
        // Drawer Header with Close Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CoachCharacterImage(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                borderWidth = 1.5.dp,
                allowPicker = true,
                showPickerIconBadge = true
            )

            IconButton(
                onClick = onCloseDrawer,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(FitCardBackground)
                    .border(1.dp, FitCardBorder, RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Drawer",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "THEFIT SHUBHAM",
            color = TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp
        )
        Text(
            text = "AI Workout & Fitness Coach",
            color = NeonPurpleGlow,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Athlete: ${userProfile?.name ?: "Shubham Rajpoot"}",
            color = TextMutedGray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(FitCardBorder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Items
        DrawerItem(
            title = "Home Dashboard",
            icon = Icons.Default.FitnessCenter,
            color = NeonPurple,
            isSelected = currentTab == BottomTab.HOME,
            onClick = {
                onSelectTab(BottomTab.HOME)
                onCloseDrawer()
            }
        )

        DrawerItem(
            title = "Workout Splits & Routines",
            icon = Icons.Default.FitnessCenter,
            color = NeonPurple,
            isSelected = currentTab == BottomTab.WORKOUT,
            onClick = {
                onSelectTab(BottomTab.WORKOUT)
                onCloseDrawer()
            }
        )

        DrawerItem(
            title = "Diet & Indian Nutrition",
            icon = Icons.Default.Restaurant,
            color = NeonGreen,
            isSelected = currentTab == BottomTab.DIET,
            onClick = {
                onSelectTab(BottomTab.DIET)
                onCloseDrawer()
            }
        )

        DrawerItem(
            title = "Activity & Biometric Radar",
            icon = Icons.Default.LocalFireDepartment,
            color = NeonCyan,
            isSelected = currentTab == BottomTab.ACTIVITY,
            onClick = {
                onSelectTab(BottomTab.ACTIVITY)
                onCloseDrawer()
            }
        )

        DrawerItem(
            title = "Running Speedometer",
            icon = Icons.Default.DirectionsRun,
            color = NeonOrange,
            isSelected = false,
            onClick = {
                onCloseDrawer()
                onOpenRunning()
            }
        )

        DrawerItem(
            title = "Meditation & Recovery",
            icon = Icons.Default.SelfImprovement,
            color = NeonCyan,
            isSelected = false,
            onClick = {
                onCloseDrawer()
                onOpenMeditation()
            }
        )

        DrawerItem(
            title = "Smart Reminders",
            icon = Icons.Default.Notifications,
            color = NeonPurpleGlow,
            isSelected = false,
            onClick = {
                onCloseDrawer()
                onOpenReminders()
            }
        )

        DrawerItem(
            title = "My Fitness Profile",
            icon = Icons.Default.Person,
            color = NeonPurple,
            isSelected = currentTab == BottomTab.PROFILE,
            onClick = {
                onSelectTab(BottomTab.PROFILE)
                onCloseDrawer()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(FitCardBorder)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Reset / Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onCloseDrawer()
                    onLogout()
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = NeonRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Reset Profile / Logout",
                color = NeonRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DrawerItem(
    title: String,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) color.copy(alpha = 0.18f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) color else TextMutedGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            color = if (isSelected) TextWhite else TextLightGray,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun NotificationsDialog(
    insights: List<AiCoachInsight>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F1524),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Coach Notifications",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                insights.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF141C30))
                            .border(1.dp, NeonPurple.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    color = TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NeonCyan.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.priorityLevel,
                                        color = NeonCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.message,
                                color = TextLightGray,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Dismiss All", color = NeonCyan, fontWeight = FontWeight.Bold)
            }
        }
    )
}
