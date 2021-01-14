package internet

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
                emit(Resource.success(name))
            } catch (e: Exception) {
                emit(Resource.error(e, null))
            }
        }
    }

    fun getFundList(fundCodes: List<String>): Flow<List<FundRealTimeInfo>> {
        return flow {
            while (true) {
                try {
                    val result = fundCodes.map {
                        coroutineScope {
                            async {
                                service.getFundTrends(it)
                            }
                        }
                    }.awaitAll()
                    emit(result)
                } catch (e: Exception) {
                    //no need to handle
                }
                delay(30000)
            }
        }
    }
}