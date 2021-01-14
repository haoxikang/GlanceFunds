package ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import internet.FundRepository
import internet.FundServiceImp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import ov.FundRealTimeInfo
import state.Resource
import state.Status

class MainViewModel {
    private var getFundListJob = Job()
    private var addFundJob = Job()
    private val fundRepository = FundRepository(FundServiceImp())

    lateinit var stateText: MutableState<TextFieldValue>
    val name = mutableStateOf(Resource.loading<FundRealTimeInfo>(null))
    val isShowItemWindow = mutableStateOf(false)
    val fundsList = mutableStateOf(listOf<FundRealTimeInfo>())

    private val fundsCode = mutableListOf("001500", "161725", "003095")

    init {
        getAllFundState()
    }

    fun updateTextFieldValue(textFieldValue: TextFieldValue) {
        if (textFieldValue.text == stateText.value.text) return
        name.value = Resource.loading(null)
        val text = textFieldValue.text
        if (text.isBlank()) {
            stateText.value = textFieldValue
            return
        }
        if (text.length > 10) return
        text.toLongOrNull() ?: return
        stateText.value = textFieldValue
    }

    fun addANewFund() {
        val text = stateText.value.text
        if (text.isBlank() || text in fundsCode) return
        addFundJob.cancel()
        addFundJob = Job()
        GlobalScope.launch(addFundJob + Dispatchers.IO) {
            fundRepository.getName(text).collect {
                withContext(Dispatchers.Main) {
                    name.value = it
                    if (it.status == Status.SUCCESS) {
                        addFund(it.data ?: return@withContext)
                        stateText.value = TextFieldValue("")
                    }
                }
            }
        }
    }

    private fun getAllFundState() {
        getFundListJob.cancel()
        getFundListJob = Job()
        GlobalScope.launch(getFundListJob + Dispatchers.IO) {
            fundRepository.getFundList(fundsCode).collectLatest {
                withContext(Dispatchers.Main) {
                    fundsList.value = it
                }
            }
        }
    }


    private fun addFund(fundRealTimeInfo: FundRealTimeInfo) {
        fundsList.value = fundsList.value + listOf(fundRealTimeInfo)
        fundsCode.add(fundRealTimeInfo.fundCode)
        getAllFundState()
    }

    fun deleteFund(code: String) {
        fundsList.value = fundsList.value.filter {
            it.fundCode != code
        }
        fundsCode.removeIf {
            it == code
        }
        getAllFundState()
    }

    fun clean() {
        addFundJob.cancel()
        getFundListJob.cancel()
    }
}