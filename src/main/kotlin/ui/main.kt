package ui

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import icAppRounded

fun main() {
    val viewModel = MainViewModel()
    Window(title = "摸鱼鱼", size = IntSize(408, 700), icon = icAppRounded()) {
        AppManager.windows.forEach {
            it.window.isResizable = false
        }
        onActive {
            onDispose {
                viewModel.clean()
            }
        }
        MaterialTheme {
            Scaffold(
                bodyContent = {
                    Column(Modifier.background(Color(255, 255, 255)).fillMaxSize()) {
                        TopView(viewModel)
                        FundInfo(viewModel)
                        Spacer(Modifier.preferredHeight(32.dp))
                        BottomView(viewModel)
                    }
                }
            )
        }
    }
}

