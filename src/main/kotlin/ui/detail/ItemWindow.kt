package ui.detail

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import blue
import ui.MainViewModel

fun FundItem(viewModel: MainViewModel, title: String, fundCode: String) {
    Window(title,size = IntSize(520,140)) {
        viewModel.fundUnitStateText = remember { mutableStateOf(TextFieldValue("")) }
        Row(
            Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                viewModel.fundUnitStateText.value,
                { s ->
                    viewModel.onInput(s)
                },
                label = {
                    Text("持有份额", fontSize = 10.sp)
                },
                modifier =
                Modifier
                    .padding(start = 16.dp)
                    .wrapContentWidth()
                    .height(48.dp),
                activeColor = blue,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = blue
                ),
            )
            Spacer(Modifier.preferredWidth(16.dp))
            Button({
                viewModel.updateFundUnit(fundCode)
                AppManager.focusedWindow?.close()
            }, content = {
                Text("更新份额", color = Color(0xffffffff))
            }, colors = ButtonDefaults.buttonColors(backgroundColor = blue))
        }

    }

}