package xyz.tberghuis.floatingtimer.tmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TmpScreen(
  vm: TmpVm = viewModel()
) {
  Column(
    modifier = Modifier.padding(horizontal = 30.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text("tmp screen")

    Button(onClick = {
      vm.initialiseRingtone()
    }) {
      Text("init ringtone")
    }

    Button(onClick = {
      vm.playRingtone()
    }) {
      Text("play ringtone")
    }


  }
}