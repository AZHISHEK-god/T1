package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import com.example.ui.components.AbhishekSignature
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.StatusRed
import com.example.ui.theme.T1Yellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsDialog(
    onOpenAdminPanel: () -> Unit,
    onOpenBackendConfig: () -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    var showPasscodePrompt by remember { mutableStateOf(false) }
    var enteredPasscode by remember { mutableStateOf("") }
    var passcodeError by remember { mutableStateOf<String?>(null) }

    if (showPasscodePrompt) {
        Dialog(onDismissRequest = { showPasscodePrompt = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, T1Yellow, RoundedCornerShape(16.dp)),
                color = CyberBlack
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "ADMIN SECURITY GATEWAY",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = T1Yellow,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Enter secret master passcode to proceed to Admin Console.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )

                    OutlinedTextField(
                        value = enteredPasscode,
                        onValueChange = {
                            enteredPasscode = it
                            passcodeError = null
                        },
                        label = { Text("PASSCODE", color = TextSecondary, fontSize = 11.sp) },
                        placeholder = { Text("••••", color = TextSecondary.copy(alpha = 0.5f)) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = T1Yellow,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    passcodeError?.let { err ->
                        Text(
                            text = err,
                            style = MaterialTheme.typography.labelSmall.copy(color = StatusRed, fontSize = 11.sp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showPasscodePrompt = false },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated, contentColor = TextSecondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("CANCEL", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (enteredPasscode == "111" || enteredPasscode.equals("ABHISHEK_ADMIN", ignoreCase = true)) {
                                    showPasscodePrompt = false
                                    onDismiss()
                                    onOpenAdminPanel()
                                } else {
                                    passcodeError = "Access Denied: Incorrect secret passcode."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = T1Yellow, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("AUTHENTICATE", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, T1Yellow, RoundedCornerShape(20.dp)),
            color = CyberBlack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(T1Yellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SETTINGS & SECURITY",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = T1Yellow,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "T1 Esports System Options",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                // Option 1: Admin Panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(0.5.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = T1Yellow, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("ADMIN PANEL", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                                Text("Key Generator & Validator", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                            }
                        }

                        Button(
                            onClick = {
                                enteredPasscode = ""
                                passcodeError = null
                                showPasscodePrompt = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = T1Yellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("OPEN", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                }

                // Option 2: Backend Config
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(0.5.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Dns, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("DATA ENDPOINTS", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                                Text("Live Free Fire ID APIs", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                onDismiss()
                                onOpenBackendConfig()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("CONFIG", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                // Option 3: LOGOUT (Locks the app)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(1.dp, StatusRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = StatusRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SESSION SECURITY",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = StatusRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Once you log out, you will need to re-enter your access key on the opening screen.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                onDismiss()
                                onLogout()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("logout_button")
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("LOG OUT & LOCK APP", fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }

                AbhishekSignature(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
