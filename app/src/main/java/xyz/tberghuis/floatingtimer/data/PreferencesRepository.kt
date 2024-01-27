package xyz.tberghuis.floatingtimer.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.tberghuis.floatingtimer.BuildConfig
import xyz.tberghuis.floatingtimer.DEFAULT_HALO_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_INNER_COLOR
import xyz.tberghuis.floatingtimer.DEFAULT_OUTER_COLOR
import xyz.tberghuis.floatingtimer.logd

val Context.dataStore by preferencesDataStore(
  name = "user_preferences",
)

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
  val vibrationFlow: Flow<Boolean> = dataStore.data.map { preferences ->
    preferences[booleanPreferencesKey("vibration")] ?: true
  }

  suspend fun updateVibration(vibration: Boolean) {
    dataStore.edit { preferences ->
      preferences[booleanPreferencesKey("vibration")] = vibration
    }
  }

  val soundFlow: Flow<Boolean> = dataStore.data.map { preferences ->
    preferences[booleanPreferencesKey("sound")] ?: true
  }

  suspend fun updateSound(sound: Boolean) {
    dataStore.edit { preferences ->
      preferences[booleanPreferencesKey("sound")] = sound
    }
  }

  val haloColourPurchasedFlow: Flow<Boolean> = dataStore.data.map { preferences ->
    preferences[booleanPreferencesKey("halo_colour_purchased")] ?: BuildConfig.DEFAULT_PURCHASED
  }

  suspend fun updateHaloColourPurchased(purchased: Boolean) {
    dataStore.edit { preferences ->
      preferences[booleanPreferencesKey("halo_colour_purchased")] = purchased
    }
  }

  val haloColourFlow: Flow<Color> = dataStore.data.map { preferences ->
    val haloColourString = preferences[stringPreferencesKey("halo_colour")]
    val haloColor = if (haloColourString == null)
      DEFAULT_HALO_COLOR
    else
      Color(haloColourString.toULong())
    logd("haloColourFlow halocolor $haloColor")
    haloColor
  }

  suspend fun updateHaloColour(color: Color) {
    logd("updateHaloColour")
    val haloColourString = color.value.toString()
    dataStore.edit { preferences ->
      preferences[stringPreferencesKey("halo_colour")] = haloColourString
    }
  }

  // todo test this
  suspend fun resetHaloColour() {
    dataStore.edit { preferences ->
      preferences.remove(stringPreferencesKey("halo_colour"))
    }
  }

  val innerColourFlow: Flow<Color> = dataStore.data.map { preferences ->
    val innerColourString = preferences[stringPreferencesKey("inner_colour")]
    val innerColor = if (innerColourString == null)
      DEFAULT_INNER_COLOR
    else
      Color(innerColourString.toULong())
    logd("outerColourFlow outercolor $innerColor")
    innerColor
  }

  suspend fun updateInnerColour(color: Color) {
    logd("updateInnerColour")
    val innerColourString = color.value.toString()
    dataStore.edit { preferences ->
      preferences[stringPreferencesKey("inner_colour")] = innerColourString
    }
  }

  val outerColourFlow: Flow<Color> = dataStore.data.map { preferences ->
    val outerColourString = preferences[stringPreferencesKey("outer_colour")]
    val outerColor = if (outerColourString == null)
      DEFAULT_OUTER_COLOR
    else
      Color(outerColourString.toULong())
    logd("outerColourFlow outercolor $outerColor")
    outerColor
  }

  suspend fun updateOuterColour(color: Color) {
    logd("updateOuterColour")
    val outerColourString = color.value.toString()
    dataStore.edit { preferences ->
      preferences[stringPreferencesKey("outer_colour")] = outerColourString
    }
  }

  val bubbleScaleFlow: Flow<Float> = dataStore.data.map { preferences ->
    preferences[floatPreferencesKey("bubble_scale")] ?: 1f
  }

  suspend fun updateBubbleScale(scale: Float) {
    dataStore.edit { preferences ->
      preferences[floatPreferencesKey("bubble_scale")] = scale
    }
  }

  suspend fun resetBubbleScale() {
    dataStore.edit { preferences ->
      preferences.remove(floatPreferencesKey("bubble_scale"))
    }
  }
}