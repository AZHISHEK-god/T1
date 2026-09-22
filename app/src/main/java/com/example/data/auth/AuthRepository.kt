package com.example.data.auth

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AccessKeyRecord(
    val key: String,
    val label: String,
    val isUsed: Boolean,
    val createdAt: String,
    val isRevoked: Boolean = false,
    val usedBy: String = "",
    val usedAt: String = ""
)

sealed interface KeyValidationResult {
    data object AdminPanel : KeyValidationResult
    data class Success(val key: String, val userName: String, val message: String) : KeyValidationResult
    data class Error(val message: String) : KeyValidationResult
}

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("t1_auth_prefs", Context.MODE_PRIVATE)

    companion object {
        const val ADMIN_PASSCODE = "111"
        const val MASTER_KEY = "ABHISHEK-VIP-777"
        private const val PREF_IS_LOGGED_IN = "pref_is_logged_in"
        private const val PREF_CURRENT_KEY = "pref_current_key"
        private const val PREF_USER_NAME = "pref_user_name"
        private const val PREF_STORED_KEYS = "pref_stored_keys_json"
        private const val PREF_LAST_ADMIN_NOTIFICATION = "pref_last_admin_notification"

        private val DEFAULT_VALID_KEYS = listOf(
            "T1-VIP-2026",
            "HEADSHOT-PRO-99",
            "T1-ESPORTS-VIP",
            "FF-MAX-ULTRA",
            MASTER_KEY
        )
    }

    init {
        // Initialize default keys if not present
        if (!prefs.contains(PREF_STORED_KEYS)) {
            val initialList = DEFAULT_VALID_KEYS.map {
                AccessKeyRecord(
                    key = it,
                    label = "System Default VIP Key",
                    isUsed = false,
                    createdAt = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date()),
                    isRevoked = false,
                    usedBy = "",
                    usedAt = ""
                )
            }
            saveKeysToPrefs(initialList)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(PREF_IS_LOGGED_IN, false)
    }

    fun getCurrentKey(): String {
        return prefs.getString(PREF_CURRENT_KEY, "") ?: ""
    }

    fun getUserName(): String {
        val stored = prefs.getString(PREF_USER_NAME, "") ?: ""
        return if (stored.isNotBlank()) stored else "Pro Player"
    }

    fun setLoggedIn(key: String, userName: String) {
        val cleanName = userName.trim().ifBlank { "Pro Player" }
        prefs.edit()
            .putBoolean(PREF_IS_LOGGED_IN, true)
            .putString(PREF_CURRENT_KEY, key)
            .putString(PREF_USER_NAME, cleanName)
            .apply()
    }

    fun getLastAdminNotification(): String? {
        return prefs.getString(PREF_LAST_ADMIN_NOTIFICATION, null)
    }

    fun clearAdminNotification() {
        prefs.edit().remove(PREF_LAST_ADMIN_NOTIFICATION).apply()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(PREF_IS_LOGGED_IN, false)
            .putString(PREF_CURRENT_KEY, "")
            .apply()
    }

    fun validateKey(rawInput: String, userName: String): KeyValidationResult {
        val cleanKey = rawInput.trim()
        val cleanName = userName.trim()

        if (cleanKey == ADMIN_PASSCODE) {
            return KeyValidationResult.AdminPanel
        }

        if (cleanName.isBlank()) {
            return KeyValidationResult.Error("Name is compulsory! Please enter your name.")
        }

        if (cleanKey.isBlank()) {
            return KeyValidationResult.Error("Please enter a valid Access Key.")
        }

        val allKeys = getAllKeys()
        val found = allKeys.find { it.key.equals(cleanKey, ignoreCase = true) }

        if (found != null) {
            if (found.isRevoked) {
                return KeyValidationResult.Error("This key has been revoked by Admin Abhishek.")
            }

            val timestamp = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
            
            // Mark key as used by this user
            val updatedKeys = allKeys.map {
                if (it.key.equals(cleanKey, ignoreCase = true)) {
                    it.copy(isUsed = true, usedBy = cleanName, usedAt = timestamp)
                } else {
                    it
                }
            }
            saveKeysToPrefs(updatedKeys)

            // Save Admin Notification
            val notification = "🔔 ALERT: Key ${found.key} activated by '$cleanName' on $timestamp"
            prefs.edit().putString(PREF_LAST_ADMIN_NOTIFICATION, notification).apply()

            // Push to Firebase Firestore so Admin sees activations across all devices
            try {
                val alertDoc = hashMapOf(
                    "key" to found.key,
                    "userName" to cleanName,
                    "timestamp" to timestamp,
                    "timeMillis" to System.currentTimeMillis()
                )
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("admin_alerts")
                    .add(alertDoc)
            } catch (ignored: Exception) {}

            setLoggedIn(found.key, cleanName)
            return KeyValidationResult.Success(found.key, cleanName, "Access Granted! Welcome to T1 Esports, $cleanName.")
        }

        return KeyValidationResult.Error("Invalid Access Key! Contact Abhishek or type '111' for Admin Panel.")
    }

    fun generateNewKey(label: String = "VIP Client"): AccessKeyRecord {
        val random = SecureRandom()
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val part1 = (1..4).map { chars[random.nextInt(chars.length)] }.joinToString("")
        val part2 = (1..4).map { chars[random.nextInt(chars.length)] }.joinToString("")
        val newKey = "T1-$part1-$part2"

        val record = AccessKeyRecord(
            key = newKey,
            label = label.ifBlank { "VIP User Key" },
            isUsed = false,
            createdAt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
            isRevoked = false
        )

        val keys = getAllKeys().toMutableList()
        keys.add(0, record)
        saveKeysToPrefs(keys)
        return record
    }

    fun revokeKey(key: String) {
        val keys = getAllKeys().map {
            if (it.key.equals(key, ignoreCase = true)) {
                it.copy(isRevoked = true)
            } else {
                it
            }
        }
        saveKeysToPrefs(keys)
    }

    fun deleteKey(key: String) {
        val keys = getAllKeys().filterNot { it.key.equals(key, ignoreCase = true) }
        saveKeysToPrefs(keys)
    }

    fun getAllKeys(): List<AccessKeyRecord> {
        val rawJson = prefs.getString(PREF_STORED_KEYS, null) ?: return emptyList()
        val list = mutableListOf<AccessKeyRecord>()
        try {
            val array = JSONArray(rawJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    AccessKeyRecord(
                        key = obj.optString("key"),
                        label = obj.optString("label"),
                        isUsed = obj.optBoolean("isUsed", false),
                        createdAt = obj.optString("createdAt"),
                        isRevoked = obj.optBoolean("isRevoked", false),
                        usedBy = obj.optString("usedBy", ""),
                        usedAt = obj.optString("usedAt", "")
                    )
                )
            }
        } catch (_: Exception) {
        }
        return list
    }

    private fun saveKeysToPrefs(keys: List<AccessKeyRecord>) {
        val array = JSONArray()
        for (k in keys) {
            val obj = JSONObject().apply {
                put("key", k.key)
                put("label", k.label)
                put("isUsed", k.isUsed)
                put("createdAt", k.createdAt)
                put("isRevoked", k.isRevoked)
                put("usedBy", k.usedBy)
                put("usedAt", k.usedAt)
            }
            array.put(obj)
        }
        prefs.edit().putString(PREF_STORED_KEYS, array.toString()).apply()
    }
}
