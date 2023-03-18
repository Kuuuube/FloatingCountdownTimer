package xyz.tberghuis.floatingtimer.service

import android.content.Context
import android.graphics.PixelFormat
import android.view.WindowManager
import kotlinx.coroutines.flow.Flow
import xyz.tberghuis.floatingtimer.OverlayViewHolder
import xyz.tberghuis.floatingtimer.TIMER_SIZE_DP
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.service.countdown.CountdownState

class OverlayController(val service: FloatingService) {

  val countdownState = CountdownState()
  private val countdownIsVisible: Flow<Boolean?>
    get() = countdownState.overlayState.isVisible


  private val density = service.resources.displayMetrics.density
  private val timerSizePx = (TIMER_SIZE_DP * density).toInt()
  private val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager

  private val countdownClickTarget = OverlayViewHolder(
    WindowManager.LayoutParams(
      timerSizePx,
      timerSizePx,
      0, // todo place default position
      0,
      WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
      PixelFormat.TRANSLUCENT
    ), service
  )

  val fullscreenOverlay: OverlayViewHolder = OverlayViewHolder(
    WindowManager.LayoutParams(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
      WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
      PixelFormat.TRANSLUCENT
    ), service
  )

  init {
    logd("OverlayController init")
//    setContentOverlayView()
//    setContentClickTargets()
//    watchState()
  }


  private fun setContentClickTargets() {

    countdownClickTarget.view.setContent {
      ClickTarget {
        logd("click target onclick")
      }
    }

  }


}