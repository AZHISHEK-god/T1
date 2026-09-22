package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("T1 ESPORTS", appName)
  }

  @Test
  fun `sensitivity calculation is within valid 0 to 200 bounds`() {
    val profile = com.example.data.model.SensitivityProfile.calculateForDevice(
      refreshRateHz = 120,
      screenDpi = 440,
      ramGb = 8,
      manufacturer = "Samsung"
    )
    assert(profile.general in 0..200)
    assert(profile.redDot in 0..200)
    assert(profile.scope2x in 0..200)
    assert(profile.scope4x in 0..200)
    assert(profile.sniperScope in 0..200)
    assert(profile.freeLook in 0..200)
  }

  @Test
  fun `budget and flagship devices generate noticeably different sensitivities`() {
    val budgetProfile = com.example.data.model.SensitivityProfile.calculateForDevice(
      refreshRateHz = 60,
      screenDpi = 280,
      ramGb = 3,
      manufacturer = "Infinix",
      model = "Hot 10"
    )
    val flagshipProfile = com.example.data.model.SensitivityProfile.calculateForDevice(
      refreshRateHz = 144,
      screenDpi = 480,
      ramGb = 16,
      manufacturer = "Asus",
      model = "ROG Phone 7"
    )

    // Budget phone requires high sensitivity (due to digitizer friction & 60Hz)
    assert(budgetProfile.general >= 190) { "Budget general was ${budgetProfile.general}" }

    // Flagship requires controlled, lower sensitivity to prevent crosshair overshoot
    assert(flagshipProfile.general <= 160) { "Flagship general was ${flagshipProfile.general}" }

    // Clearly distinct
    assert(budgetProfile.general - flagshipProfile.general >= 30) {
      "Expected significant variance between devices but got ${budgetProfile.general} vs ${flagshipProfile.general}"
    }
  }

  @Test
  fun `character skill database contains essential roles`() {
    val roles = com.example.data.model.CharacterSkillDatabase.combinations
    assert(roles.isNotEmpty())
    assert(roles.any { it.roleId == "rusher" })
    assert(roles.any { it.roleId == "sniper" })
  }

  @Test
  fun `uid inspector repository defaults to redx live checker`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = com.example.data.network.UidInspectorRepository(context)
    assertEquals("", repository.configuredEndpoint)
  }

  @Test
  fun `admin passcode 111 triggers AdminPanel result`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val authRepository = com.example.data.auth.AuthRepository(context)
    val result = authRepository.validateKey("111", "AbhishekAdmin")
    assert(result is com.example.data.auth.KeyValidationResult.AdminPanel)
  }

  @Test
  fun `scenario skill database contains CS and BR scenarios`() {
    val scenarios = com.example.data.model.ScenarioSkillDatabase.scenarios
    assert(scenarios.isNotEmpty())
    assert(scenarios.any { it.id == "cs_rush" })
    assert(scenarios.any { it.id == "br_survival" })
  }
}
