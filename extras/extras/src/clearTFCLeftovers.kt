import com.google.gson.Gson
import java.io.File

data class JsonData(
    var replace: Boolean = false,
    var values: ArrayList<String> = ArrayList()
)


fun main() {
    leftovers.forEach { folderPath ->
        processFilesInFolder(folderPath)
    }
}

fun processFilesInFolder(folderPath: String) {
    val folder = File(folderPath)

    if (folder.exists() && folder.isDirectory) {
        val filesAndDirs = folder.listFiles()

        filesAndDirs?.forEach { fileOrDir ->
            if (fileOrDir.isFile) {
                leftoversSpruceUp(fileOrDir.absolutePath)
            } else if (fileOrDir.isDirectory) {
                processFilesInFolder(fileOrDir.absolutePath)
            }
        }
    } else {
        println("Folder $folderPath does not exist or is not a directory.")
    }
}


fun leftoversSpruceUp(jsonFilePath: String) {
    val jsonString = File(jsonFilePath).readText()

    val jsonData = gson.fromJson(jsonString, JsonData::class.java)

    if( jsonData.values.isEmpty() ){
        println("Values from json empty")
        println("$jsonFilePath")
        return
    }


    val filteredValues = jsonData.values.filter { value ->
        getKeys().any { mineral -> value.contains(mineral) }
    }


    jsonData.values.clear()
    jsonData.values.addAll(filteredValues)

    val updatedJsonString = gson.toJson(jsonData)
    File(jsonFilePath).writeText(updatedJsonString)

    println("JSON file updated successfully.")
    println("$jsonFilePath")
}
