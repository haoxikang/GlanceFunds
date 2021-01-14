package ov

import com.google.gson.annotations.SerializedName

data class FundRealTimeInfo(
    @SerializedName("fundcode")val fundCode:String,
    @SerializedName("name")val name:String,
    @SerializedName("jzrq")val startTime:String,
    @SerializedName("dwjz")val netWorth:String,
    @SerializedName("gsz")val EstimatedNetWorth:String,
    @SerializedName("gszzl")val EstimatedRiseAndFall:String,
    @SerializedName("gztime")val EstimatedTime:String
)