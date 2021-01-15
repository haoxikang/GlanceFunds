package internet

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ov.FundRealTimeInfo
import state.Resource

class FundRepository(private val service: FundsService) {
    fun getName(fundCode: String): Flow<Resource<FundRealTimeInfo>> {
        return flow {
            emit(Resource.loading(null))
            try {
                val name = service.getFundName(fundCode)
                name.fundUnit = mutableStateOf(0f)
                emit(Resource.success(name))
            } catch (e: Exception) {
                emit(Resource.error(e, null))
            }
        }
    }

    fun getFundList(fundCodes: List<Pair<String, Float>>): Flow<List<FundRealTimeInfo>> {
        return flow {
            while (true) {
                try {
                    val result = fundCodes.map {
                        coroutineScope {
                            async {
                                val data = service.getFundTrends(it.first)
                                data.fundUnit = mutableStateOf(it.second)
                                data
                            }
                        }
                    }.awaitAll()
                    emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(10000)
            }
        }
    }
}