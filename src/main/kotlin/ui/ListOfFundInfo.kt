package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import getFlagColor
import ui.detail.FundItem

@Composable
fun FundInfo(viewModel: MainViewModel) {
    LazyColumn(modifier = Modifier.height(300.dp)) {
        items(viewModel.fundsList.value) { info ->
            val isFall = info.estimatedRiseAndFall.toFloatOrNull() ?: 0f < 0
            val currentColor = getFlagColor(isFall)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        FundItem(viewModel, info.name, info.fundCode)
                    }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)) {
                Image(
                    bitmap = imageResource("circle.png"),
                    modifier =
                    Modifier
                        .clickable {
                            viewModel.deleteFund(info.fundCode)
                        }
                        .preferredHeight(14.dp)
                        .preferredWidth(14.dp),
                    colorFilter = ColorFilter.tint(Color(0xfff9d423))
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.preferredWidth(16.dp))
                        Text(
                            text = info.name,
                            modifier = Modifier
                                .preferredWidth(200.dp),
                            fontWeight = FontWeight.Thin,
                            fontSize = 16.sp,
                        )
                        Spacer(Modifier.preferredHeight(8.dp))

                        Spacer(Modifier.preferredWidth(16.dp))
                        Text(
                            text = info.estimatedNetWorth,
                            modifier = Modifier
                                .preferredWidth(60.dp),
                            fontSize = 14.sp,
                            color = currentColor
                        )
                        Spacer(Modifier.preferredWidth(16.dp))
                        val text =
                            if (info.estimatedRiseAndFall.toFloatOrNull() ?: 0f < 0)
                                "${info.estimatedRiseAndFall}%"
                            else
                                "+${info.estimatedRiseAndFall}%"
                        Text(
                            text = text,
                            modifier = Modifier
                                .preferredWidth(60.dp),
                            fontSize = 14.sp,
                            color = currentColor
                        )
                        Spacer(Modifier.preferredWidth(16.dp))
                    }
                    Spacer(Modifier.preferredHeight(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = if (info.fundUnit.value == 0f) "基金代码：${info.fundCode}" else "今日金额估算：${info.getTodayEstimateValue()}",
                        fontWeight = FontWeight.Thin,
                        color = if (info.fundUnit.value == 0f) Color(200, 200, 200) else currentColor,
                        fontSize = 12.sp
                    )
                }
            }
        }


    }
}