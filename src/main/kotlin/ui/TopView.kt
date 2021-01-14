package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import blue
import red
import state.Status

@Composable
fun TopView(viewModel: MainViewModel) {
    Box(
        modifier = Modifier.padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            "今日估值",
            fontSize = 14.sp,
            color = Color(150, 150, 150),
            modifier = Modifier
                .align(Alignment.CenterStart)
        )
        Row(
            Modifier
                .preferredWidth(140.dp)
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewModel.stateText = remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                viewModel.stateText.value,
                { s ->
                    viewModel.updateTextFieldValue(s)
                },
                label = {
                    Text("基金代码", fontSize = 10.sp)
                },
                modifier =
                Modifier
                    .weight(1f)
                    .height(48.dp),
                activeColor = blue,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = if (viewModel.name.value.status == Status.ERROR) red else blue
                ),
                isErrorValue = viewModel.name.value.status == Status.ERROR,
            )
            Spacer(Modifier.width(16.dp))
            Image(
                bitmap = imageResource("add.png"),
                Modifier
                    .preferredWidth(24.dp)
                    .preferredWidth(24.dp)
                    .clickable {
                        viewModel.addANewFund()
                    },
            )
        }
    }
}