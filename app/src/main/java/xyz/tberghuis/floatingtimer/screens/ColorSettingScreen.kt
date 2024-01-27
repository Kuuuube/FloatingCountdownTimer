package xyz.tberghuis.floatingtimer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godaddy.android.colorpicker.ClassicColorPicker
import xyz.tberghuis.floatingtimer.LocalNavController
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.composables.PremiumDialog
import xyz.tberghuis.floatingtimer.composables.SettingsTimerPreviewCard
import xyz.tberghuis.floatingtimer.viewmodels.ColorSettingViewModel
import xyz.tberghuis.floatingtimer.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSettingScreen(
  vm: ColorSettingViewModel = viewModel()
) {
  val navController = LocalNavController.current

  val topBarTitle = when (vm.timerType) {
    "stopwatch" -> stringResource(R.string.stopwatch_timer_color)
    "countdown" -> stringResource(R.string.countdown_timer_color)
    else -> stringResource(R.string.default_timer_color)
  }

  Scaffold(
    modifier = Modifier,
    topBar = {
      TopAppBar(
        title = { Text(topBarTitle) },
        navigationIcon = {
          IconButton(onClick = {
            navController.navigateUp()
          }) {
            Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
          }
        },
        modifier = Modifier,
      )
    },
    snackbarHost = {},
  ) { padding ->
    ColorSettingScreenContent(padding)
  }
}

@Composable
fun ColorSettingScreenContent(
  padding: PaddingValues,
  vm: ColorSettingViewModel = viewModel()
) {
  if (!vm.initialised) {
    return
  }

  Column(
    modifier = Modifier
      .padding(padding)
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {

    Column(
      modifier = Modifier
        .widthIn(0.dp, 350.dp)
        .padding(15.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      SettingsTimerPreviewCard(vm.settingsTimerPreviewVmc)
      Text(stringResource(R.string.halo) + " 0x" +
          String.format("%X", (vm.haloColorPickerColorState.value.toColor().alpha * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.haloColorPickerColorState.value.toColor().red * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.haloColorPickerColorState.value.toColor().green * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.haloColorPickerColorState.value.toColor().blue * 255).toInt()).padStart(2, '0')
      )
      ClassicColorPicker(
        modifier = Modifier
          .height(300.dp)
          .fillMaxWidth(),
        colorState = vm.haloColorPickerColorState
      )
      Text(stringResource(R.string.inner) + " 0x" +
          String.format("%X", (vm.innerColorPickerColorState.value.toColor().alpha * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.innerColorPickerColorState.value.toColor().red * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.innerColorPickerColorState.value.toColor().green * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.innerColorPickerColorState.value.toColor().blue * 255).toInt()).padStart(2, '0')
      )
      ClassicColorPicker(
        modifier = Modifier
          .height(300.dp)
          .fillMaxWidth(),
        colorState = vm.innerColorPickerColorState
      )
      Text(stringResource(R.string.outer) + " 0x" +
          String.format("%X", (vm.outerColorPickerColorState.value.toColor().alpha * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.outerColorPickerColorState.value.toColor().red * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.outerColorPickerColorState.value.toColor().green * 255).toInt()).padStart(2, '0') +
          String.format("%X", (vm.outerColorPickerColorState.value.toColor().blue * 255).toInt()).padStart(2, '0')
      )
      ClassicColorPicker(
        modifier = Modifier
          .height(300.dp)
          .fillMaxWidth(),
        colorState = vm.outerColorPickerColorState
      )
      ColorSettingScreenActions()
    }
  }

  // this is wack
  LaunchedEffect(vm) {
    snapshotFlow {
      vm.haloColorPickerColorState.value
    }.collect {
      vm.settingsTimerPreviewVmc.haloColor = it.toColor()
    }
  }
  LaunchedEffect(vm) {
    snapshotFlow {
      vm.innerColorPickerColorState.value
    }.collect {
      vm.settingsTimerPreviewVmc.innerColor = it.toColor()
    }
  }
  LaunchedEffect(vm) {
    snapshotFlow {
      vm.outerColorPickerColorState.value
    }.collect {
      vm.settingsTimerPreviewVmc.outerColor = it.toColor()
    }
  }
}

@Composable
fun ColorSettingScreenActions(
  vm: ColorSettingViewModel = viewModel()
) {

  val nav = LocalNavController.current
  // safe: no other way to get to this screen
  val homeVm: HomeViewModel = viewModel(nav.previousBackStackEntry!!)

  val ifPremiumCallback = when (vm.timerType) {
    "stopwatch" -> {
      {
        homeVm.stopwatchHaloColor = vm.settingsTimerPreviewVmc.haloColor
        homeVm.stopwatchInnerColor = vm.settingsTimerPreviewVmc.innerColor
        homeVm.stopwatchOuterColor = vm.settingsTimerPreviewVmc.outerColor
        nav.popBackStack()
      }
    }

    "countdown" -> {
      {
        homeVm.countdownHaloColor = vm.settingsTimerPreviewVmc.haloColor
        homeVm.countdownInnerColor = vm.settingsTimerPreviewVmc.innerColor
        homeVm.countdownOuterColor = vm.settingsTimerPreviewVmc.outerColor
        nav.popBackStack()
      }
    }

    else -> {
      {
        vm.saveDefaultHaloColor()
        // doitwrong
        homeVm.countdownHaloColor = vm.settingsTimerPreviewVmc.haloColor
        homeVm.stopwatchHaloColor = vm.settingsTimerPreviewVmc.haloColor
        homeVm.countdownInnerColor = vm.settingsTimerPreviewVmc.innerColor
        homeVm.stopwatchInnerColor = vm.settingsTimerPreviewVmc.innerColor
        homeVm.countdownOuterColor = vm.settingsTimerPreviewVmc.outerColor
        homeVm.stopwatchOuterColor = vm.settingsTimerPreviewVmc.outerColor
        nav.popBackStack()
      }
    }
  }

  Row(
    horizontalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Button(onClick = {
      nav.popBackStack()
    }) {
      Text(stringResource(R.string.cancel).uppercase())
    }
    Button(onClick = {
      vm.okButtonClick {
        ifPremiumCallback()
      }
    }) {
      Text(stringResource(R.string.save).uppercase())
    }
  }

  PremiumDialog(vm.premiumVmc, stringResource(R.string.premium_reason_halo_colour)) {
    ifPremiumCallback()
  }
}