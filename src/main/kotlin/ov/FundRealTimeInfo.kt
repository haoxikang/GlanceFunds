package ov

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName

data class FundRealTimeInfo(
    @SerializedName("fundcode") val fundCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("jzrq") val startTime: String,
    @SerializedName("dwjz") val netWorth: String,
    @SerializedName("gsz") val estimatedNetWorth: String,
    @SerializedName("gszzl") val estimatedRiseAndFall: String,
    @SerializedName("gztime") val estimatedTime: String,
    var fundUnit: MutableState<Float> = mutableStateOf(0f)
) {
    fun getTodayEstimateValue(): Float {
        if (fundUnit.value == 0f) return 0f
        val value = netWorth.toFloatOrNull() ?: 0f
        val iseAndFall = estimatedRiseAndFall.toFloatOrNull() ?: 0f
        return (fundUnit.value * value * (1 + (iseAndFall) / 100))
    }

    fun getActuallyValue(): Float {
        if (fundUnit.value == 0f) return 0f
        val value = netWorth.toFloatOrNull() ?: 0f
        return fundUnit.value * value
    }
}