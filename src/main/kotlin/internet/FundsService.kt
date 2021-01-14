package internet

import ov.FundRealTimeInfo
import state.Resource

interface FundsService {
    suspend fun getFundName(fundCode: String): FundRealTimeInfo
    suspend fun getFundTrends(fundCode: String): FundRealTimeInfo
}