package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AbhishekAnimatedBanner
import com.example.ui.components.AbhishekSignature
import com.example.ui.theme.CardBorderGradient
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardGradient
import com.example.ui.theme.NeonPink
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.T1GoldGradient
import com.example.ui.theme.T1Yellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.NetworkState

@Composable
fun KeyAuthScreen(
    networkState: NetworkState,
    onKeySubmitted: (key: String, userName: String) -> Unit,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    var nameInput by remember { mutableStateOf("") }
    var keyInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val infiniteTransition = rememberInfiniteTransition(label = "cyber_effects")
    val logoGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    val rotateRings by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate_rings"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val handleSubmit = {
        keyboardController?.hide()
        onKeySubmitted(keyInput, nameInput)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .statusBarsPadding()
            .padding(top = 8.dp)
            .navigationBarsPadding()
            .imePadding()
            .testTag("key_auth_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ==================== TOP HERO SECTION ====================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                // Cyber glowing rings around logo
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(110.dp)
                ) {
                    // Outer rotating decorative cyber ring
                    Box(
                        modifier = Modifier
                            .size(108.dp)
                            .rotate(rotateRings)
                            .border(
                                width = 1.5.dp,
                                brush = Brush.sweepGradient(
                                    listOf(T1Yellow, CyberCyan, NeonPink, T1Yellow)
                                ),
                                shape = CircleShape
                            )
                    )

                    // Middle pulsing glow
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .scale(logoGlowScale)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        T1Yellow.copy(alpha = 0.35f * pulseAlpha),
                                        CyberCyan.copy(alpha = 0.15f),
                                        Color.Black
                                    )
                                )
                            )
                            .border(2.dp, T1Yellow, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.t1_logo),
                            contentDescription = "T1 Esports Logo",
                            modifier = Modifier.size(68.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Brand Title with Gold Glow
                Text(
                    text = "T1 ESPORTS",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = T1Yellow,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // System Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF131722))
                        .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StatusGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VIP GATEWAY • NO CAPTCHA • DIRECT ACCESS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==================== CENTER GLASSMORPHIC AUTH CARD ====================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkCardGradient)
                    .border(1.5.dp, CardBorderGradient, RoundedCornerShape(20.dp))
                    .padding(22.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = T1Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SECURITY ACCESS TERMINAL",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Mandatory Name Input Field
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = {
                            Text(
                                "YOUR PLAYER NAME (COMPULSORY)",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        placeholder = {
                            Text(
                                "e.g. Abhishek Pro, Toxic, Viper",
                                color = TextSecondary.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = CyberCyan
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = CyberCyan,
                            focusedContainerColor = Color(0xFF0F121C),
                            unfocusedContainerColor = Color(0xFF0C0E17)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Key Input Field (With Paste from Clipboard button)
                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        label = {
                            Text(
                                "ENTER ACCESS KEY",
                                color = T1Yellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        placeholder = {
                            Text(
                                "e.g. T1-VIP-2026 or 111",
                                color = TextSecondary.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = T1Yellow
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    try {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = clipboard.primaryClip
                                        if (clip != null && clip.itemCount > 0) {
                                            val pasted = clip.getItemAt(0).text?.toString()?.trim()
                                            if (!pasted.isNullOrBlank()) {
                                                keyInput = pasted
                                            }
                                        }
                                    } catch (_: Exception) {}
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste Key",
                                    tint = T1Yellow,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("key_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleSubmit() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = T1Yellow,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = T1Yellow,
                            focusedContainerColor = Color(0xFF0F121C),
                            unfocusedContainerColor = Color(0xFF0C0E17)
                        )
                    )

                    // Error Message banner
                    AnimatedVisibility(visible = !errorMessage.isNullOrBlank()) {
                        errorMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(StatusRed.copy(alpha = 0.15f))
                                    .border(1.dp, StatusRed.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = StatusRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StatusRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 100000x Unlock Button with Gold Gradient
                    Button(
                        onClick = { handleSubmit() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(T1GoldGradient, RoundedCornerShape(12.dp))
                            .testTag("unlock_app_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "UNLOCK T1 ESPORTS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
            }

            // Network Offline Warning
            if (!networkState.isConnected) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(StatusRed.copy(alpha = 0.2f))
                        .border(1.dp, StatusRed, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Icon(Icons.Default.WifiOff, contentDescription = null, tint = StatusRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Active internet connection is mandatory to authenticate and run T1 Esports.",
                        style = MaterialTheme.typography.bodySmall.copy(color = StatusRed, fontSize = 11.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==================== BOTTOM BRANDING & DEVELOPER ====================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                AbhishekAnimatedBanner()
                Spacer(modifier = Modifier.height(10.dp))
                AbhishekSignature()
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Need a Key? Contact Abhishek on Instagram or Telegram",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
