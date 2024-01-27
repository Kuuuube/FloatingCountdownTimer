package xyz.tberghuis.floatingtimer.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.godaddy.android.colorpicker.HsvColor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.DEFAULT_ACTIVE_FONT_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_HALO_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_INACTIVE_FONT_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_INNER_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_OUTER_COLOR
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.providePreferencesRepository

class ColorSettingViewModel(application: Application, savedStateHandle: SavedStateHandle) :
  AndroidViewModel(application) {
  private val preferences = application.providePreferencesRepository()
  var haloColorPickerColorState = mutableStateOf(HsvColor.from(DEFAULT_HALO_COLOR))
  var innerColorPickerColorState = mutableStateOf(HsvColor.from(DEFAULT_INNER_COLOR))
  var outerColorPickerColorState = mutableStateOf(HsvColor.from(DEFAULT_OUTER_COLOR))
  var activeFontColorPickerColorState = mutableStateOf(HsvColor.from(DEFAULT_ACTIVE_FONT_COLOR))
  var inactiveFontColorPickerColorState = mutableStateOf(HsvColor.from(DEFAULT_INACTIVE_FONT_COLOR))


  val premiumVmc = PremiumVmc(application, viewModelScope)

  private val premiumFlow = application.providePreferencesRepository().haloColourPurchasedFlow

  var initialised by mutableStateOf(false)
  lateinit var settingsTimerPreviewVmc: SettingsTimerPreviewVmc

  // null == default color
  val timerType: String? = savedStateHandle["timerType"]

  init {
    logd("ColorSettingViewModel timerType $timerType")

    viewModelScope.launch {
      val haloColor = preferences.haloColourFlow.first()
      haloColorPickerColorState.value = HsvColor.from(haloColor)
      val innerColor = preferences.innerColourFlow.first()
      innerColorPickerColorState.value = HsvColor.from(innerColor)
      val outerColor = preferences.outerColourFlow.first()
      outerColorPickerColorState.value = HsvColor.from(outerColor)
      val activeFontColor = preferences.activeFontColourFlow.first()
      activeFontColorPickerColorState.value = HsvColor.from(activeFontColor)
      val inactiveFontColor = preferences.inactiveFontColourFlow.first()
      inactiveFontColorPickerColorState.value = HsvColor.from(inactiveFontColor)
      val scale = preferences.bubbleScaleFlow.first()
      settingsTimerPreviewVmc = SettingsTimerPreviewVmc(scale, haloColor, innerColor, outerColor, activeFontColor, inactiveFontColor)
      initialised = true
    }
  }

  fun saveDefaultHaloColor() {
    viewModelScope.launch {
      preferences.updateHaloColour(haloColorPickerColorState.value.toColor())
      preferences.updateInnerColour(innerColorPickerColorState.value.toColor())
      preferences.updateOuterColour(outerColorPickerColorState.value.toColor())
      preferences.updateActiveFontColour(activeFontColorPickerColorState.value.toColor())
      preferences.updateInactiveFontColour(inactiveFontColorPickerColorState.value.toColor())
    }
  }

  fun okButtonClick(ifPremiumCallback: () -> Unit) {
    viewModelScope.launch {
      logd("saveHaloColorClick")
      if (premiumFlow.first()) {
        ifPremiumCallback()
      } else {
        premiumVmc.showPurchaseDialog = true
      }
    }
  }
}