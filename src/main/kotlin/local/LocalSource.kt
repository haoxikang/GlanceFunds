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
    suspend fun getFundCodes(): List<String>? {
        return withContext(Dispatchers.IO) {
            val file = File("$path/$fundCodesFileName")
            if (file.exists()) {
                val json = file.readText()
                val listType = object : TypeToken<List<String>>() {}.type
                gson.fromJson<List<String>>(json, listType)
            } else {
                null
            }
        }
    }

    suspend fun saveFundCodes(fundCode: List<String>) {
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