package internet

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ov.FundRealTimeInfo

class FundServiceImp : FundsService {
    val httpClient = HttpClient {
        expectSuccess = false
        ResponseObserver {
            println("HTTP status: ${it.status.value}")
        }
    }
    private val gson = Gson()

    override suspend fun getFundName(fundCode: String): FundRealTimeInfo {
        return callFundBriefApi(fundCode)
    }

    override suspend fun getFundTrends(fundCode: String): FundRealTimeInfo {
        return callFundBriefApi(fundCode)
    }

    private suspend fun callFundBriefApi(fundCode: String): FundRealTimeInfo{
        return withContext(Dispatchers.IO) {
            val htmlContent = httpClient.request<String> {
                url("http://fundgz.1234567.com.cn/js/$fundCode.js?rt=1463558676006")
                method = HttpMethod.Get
            }
            val result = formatResult(htmlContent)
            val fundRealTimeInfo = gson.fromJson(result, FundRealTimeInfo::class.java)
            fundRealTimeInfo
        }
    }


    private fun formatResult(response: String): String {
        val s = response.replace("jsonpgz(", "")
        return s.replace(");", "")
    }
}