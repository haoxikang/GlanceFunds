package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import green
import red

@Composable
fun BottomView(viewModel: MainViewModel) {
    Card(
        elevation = 32.dp,
        modifier = Modifier
            .fillMaxWidth()
            .preferredHeight(200.dp)
            .padding(start = 32.dp, end = 32.dp),
        shape = RoundedCornerShape(32.dp)

    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = viewModel.totalEstimateEarnings.value,
                    fontWeight = FontWeight.SemiBold,
                    color = if (viewModel.isGain.value) red else green,
                    fontSize = 32.sp,
                    style = TextStyle(letterSpacing = 3.sp)
                )
                Spacer(Modifier.preferredHeight(24.dp))
                Text(
                    text = "估算余额：${viewModel.totalActuallyBalance.value}",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(200, 200, 200),
                    fontSize = 18.sp
                )
            }

        }

    }
}