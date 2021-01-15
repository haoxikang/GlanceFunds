package ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import internet.FundRepository
import internet.FundServiceImp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import local.LocalSource
import ov.FundRealTimeInfo
import state.Resource
import state.Status
import java.math.RoundingMode
import java.text.DecimalFormat

class MainViewModel {
    private var getFundListJob = Job()
    private var addFundJob = Job()
    private val job = Job()
    private val fundRepository = FundRepository(FundServiceImp())
    private val localSource = LocalSource()

    lateinit var stateText: MutableState<TextFieldValue>
    val name = mutableStateOf(Resource.loading<FundRealTimeInfo>(null))
    val fundsList = mutableStateOf(listOf<FundRealTimeInfo>())

    private val fundsCode = mutableListOf<Pair<String, Float>>()

    lateinit var fundUnitStateText: MutableState<TextFieldValue>

    val totalEstimateEarnings = mutableStateOf("")
    val totalActuallyBalance = mutableStateOf("")
    val isGain = mutableStateOf(false)


    init {
        getAllFundState()
    }

    fun onInput(textFieldValue: TextFieldValue) {
        if (textFieldValue.text == fundUnitStateText.value.text) return
        val text = textFieldValue.text
        if (text.isBlank()) {
            fundUnitStateText.value = textFieldValue
            return
        }
        text.toFloatOrNull() ?: return
        fundUnitStateText.value = textFieldValue
    }

    fun updateFundUnit(fundCode: String) {
        fundsList.value.firstOrNull {
            it.fundCode == fundCode
        }?.fundUnit?.value = fundUnitStateText.value.text.toFloatOrNull() ?: 0f
        updateEstimate()
        GlobalScope.launch(job + Dispatchers.IO) {
            val mutableFundsCode = fundsCode.toMutableList()
            val index = mutableFundsCode.indexOfFirst { it.first == fundCode }
            if (index != -1) {
                mutableFundsCode[index] = Pair(fundCode, fundUnitStateText.value.text.toFloatOrNull() ?: 0f)
            }
            fundsCode.clear()
            fundsCode.addAll(mutableFundsCode)
            localSource.saveFundCodes(fundsCode)
        }
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
            val localData = localSource.getFundCodes() ?: return@launch
            fundsCode.apply {
                clear()
                addAll(localData)
            }
            fundRepository.getFundList(fundsCode).collectLatest {
                withContext(Dispatchers.Main) {
                    fundsList.value = it
                    updateEstimate()
                }
            }
        }
    }


    private fun updateEstimate(){
        val earnings = getTotalEstimateBalance() - getTotalActuallyBalance()
        isGain.value = earnings >= 0
        totalActuallyBalance.value = getTotalActuallyBalance().toString()

       val df =  DecimalFormat("0.00").apply {
           roundingMode = RoundingMode.HALF_UP
        }
        totalEstimateEarnings.value = df.format(earnings)
    }

    private fun addFund(fundRealTimeInfo: FundRealTimeInfo) {
        fundsList.value = fundsList.value + listOf(fundRealTimeInfo)
        fundsCode.add(Pair(fundRealTimeInfo.fundCode, 0f))
        updateEstimate()
        GlobalScope.launch(job) {
            localSource.saveFundCodes(fundsCode)
            getAllFundState()
        }
    }

    fun deleteFund(code: String) {
        fundsList.value = fundsList.value.filter {
            it.fundCode != code
        }
        fundsCode.removeIf {
            it.first == code
        }
        updateEstimate()
        GlobalScope.launch(job) {
            localSource.saveFundCodes(fundsCode)
            getAllFundState()
        }
    }

    private fun getTotalEstimateBalance(): Float {
        return fundsList.value.map {
            it.getTodayEstimateValue()
        }.sum()
    }

    private fun getTotalActuallyBalance(): Float {
        return fundsList.value.map {
            it.getActuallyValue()
        }.sum()
    }

    fun clean() {
        job.cancel()
        addFundJob.cancel()
        getFundListJob.cancel()
    }
}