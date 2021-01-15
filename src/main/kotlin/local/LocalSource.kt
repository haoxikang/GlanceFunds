package local

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import com.google.gson.reflect.TypeToken


class LocalSource {
    private val gson = Gson()
    private val path = "${JFileChooser().currentDirectory.toPath()}/glance_fund_data"
    private val fundCodesFileName = "fundCodes.txt"
    suspend fun getFundCodes(): List<Pair<String,Float>>? {
        return withContext(Dispatchers.IO) {
            val file = File("$path/$fundCodesFileName")
            if (file.exists()) {
                val json = file.readText()
                val listType = object : TypeToken<List<Pair<String,Float>>>() {}.type
                gson.fromJson<List<Pair<String,Float>>>(json, listType)
            } else {
                null
            }
        }
    }

    suspend fun saveFundCodes(fundCode: List<Pair<String,Float>>) {
        withContext(Dispatchers.IO) {
            val folder = File(path)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File("$path/$fundCodesFileName")
            if (!file.exists()) {
                file.createNewFile()
            }
            val json = gson.toJson(fundCode)
            file.writeText(json)
        }

    }
}