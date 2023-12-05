package xyz.tberghuis.floatingtimer.tmp2

import androidx.compose.runtime.compositionLocalOf
import xyz.tberghuis.floatingtimer.service.FloatingService

val LocalFloatingService = compositionLocalOf<FloatingService> {
  error("CompositionLocal LocalFloatingService not present")
}