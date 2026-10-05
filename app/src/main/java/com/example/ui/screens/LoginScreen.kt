package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.CoachPose
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite

@Composable
fun LoginScreen(
    mobileNumber: String,
    countryCode: String,
    isOtpSent: Boolean,
    enteredOtp: String,
    otpCountdown: Int,
    errorMessage: String?,
    onMobileChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    onOtpChanged: (String) -> Unit,
    onVerifyOtp: () -> Unit,
    onQuickDemoOtp: () -> Unit,
    onBackToMobile: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .testTag("login_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Back button if in OTP mode
            if (isOtpSent) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToMobile,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(FitCardBackground)
                            .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
                            .testTag("back_to_mobile_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Edit Mobile Number",
                        color = TextMutedGray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3D Master Character Coach Shubham Brand Emblem
            CoachCharacterImage(
                modifier = Modifier.size(100.dp, 125.dp),
                shape = RoundedCornerShape(22.dp),
                borderColor = NeonPurpleGlow,
                borderWidth = 1.5.dp,
                pose = CoachPose.IDLE,
                showPoseBadge = true,
                customBadgeText = "COACH SHUBHAM",
                allowPicker = false
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title & Subtitle
            Text(
                text = if (isOtpSent) "Verify Your Number" else "Welcome to TheFit Shubham",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isOtpSent) "Enter the 6-digit OTP sent to $countryCode $mobileNumber" else "Your AI Workout & Fitness Coach",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Card: Mobile Number or OTP
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isOtpSent) {
                        // MOBILE NUMBER STEP
                        Text(
                            text = "Enter Mobile Number",
                            color = TextLightGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Country Code + Phone Input Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF090D17))
                                .border(1.2.dp, FitCardBorderNeon.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Country Code (+91 India Default)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonPurple.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "🇮🇳 $countryCode",
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(FitCardBorder)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Mobile Number Input (Digits only)
                            BasicTextField(
                                value = mobileNumber,
                                onValueChange = onMobileChanged,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("mobile_number_input"),
                                textStyle = TextStyle(
                                    color = TextWhite,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                cursorBrush = SolidColor(NeonCyan),
                                decorationBox = { innerTextField ->
                                    if (mobileNumber.isEmpty()) {
                                        Text(
                                            text = "98765 43210",
                                            color = TextDarkGray,
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )

                            if (mobileNumber.length == 10) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Valid",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "No password or email required • Instant OTP login",
                            color = TextDarkGray,
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage,
                                color = NeonRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Send OTP Button
                        GlowingGradientButton(
                            text = "Send OTP",
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Phone,
                            testTag = "send_otp_button",
                            onClick = onSendOtp
                        )
                    } else {
                        // OTP VERIFICATION STEP
                        Text(
                            text = "Enter 6-digit OTP",
                            color = TextLightGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 6-digit OTP display boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (i in 0 until 6) {
                                val digit = enteredOtp.getOrNull(i)?.toString() ?: ""
                                val isFocused = enteredOtp.length == i
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF090D17))
                                        .border(
                                            width = if (isFocused) 1.8.dp else 1.dp,
                                            color = if (isFocused) NeonCyan else if (digit.isNotEmpty()) NeonPurple else FitCardBorder,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = digit,
                                        color = TextWhite,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        // Hidden real text field for keyboard capture
                        Box(modifier = Modifier.height(0.dp)) {
                            BasicTextField(
                                value = enteredOtp,
                                onValueChange = onOtpChanged,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier.testTag("otp_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Quick Demo OTP autofill button for frictionless evaluation
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonCyan.copy(alpha = 0.12f))
                                .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .clickable { onQuickDemoOtp() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("quick_demo_otp_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Auto-fill Demo OTP (123456)",
                                    color = NeonCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage,
                                color = NeonRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Resend Countdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (otpCountdown > 0) {
                                Text(
                                    text = "Resend OTP in ",
                                    color = TextMutedGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${otpCountdown}s",
                                    color = NeonPurpleGlow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "Resend OTP",
                                    color = NeonPurpleGlow,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { onSendOtp() }
                                        .padding(4.dp)
                                        .testTag("resend_otp_button")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Verify OTP Button
                        GlowingGradientButton(
                            text = "Verify OTP",
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Security,
                            testTag = "verify_otp_button",
                            onClick = onVerifyOtp
                        )
                    }
                }
            }
        }
    }
}
