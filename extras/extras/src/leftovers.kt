import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.nio.file.Path

data class JsonData(
    var replace: Boolean = false,
    var values: ArrayList<String> = ArrayList()
)

fun main() {
    leftovers()
}



fun leftovers() {
    leftovers.forEach { folderPath ->
        processFilesInFolder(folderPath)
    }
    clearLeftoversInTags()
}

private fun processFilesInFolder(folderPath: String) {
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


private fun leftoversSpruceUp(jsonFilePath: String) {
    val jsonString = File(jsonFilePath).readText()

    val jsonData = gson.fromJson(jsonString, JsonData::class.java)

    if (jsonData.values.isEmpty()) {
        println("Values from json empty")
        println("$jsonFilePath")
        return
    }



    if (getKeys().any { mineral -> File(jsonFilePath).name.contains(mineral) }) {
        println("File name contains key, passed")
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

val extraLeftovers = listOf(
    "tfc:cauldron/metal/lead", "tfc:calcite"
)

//mineable\pickaxe.json contains unwanted elements
fun clearLeftoversInTags(){
    val mineableFile = Path.of(destinationFolder.path + "\\data\\minecraft\\tags\\blocks\\mineable\\pickaxe.json")

    val json = mineableFile.toFile().readText()
    val jsonData = gson.fromJson(json, JsonData::class.java)

    extraLeftovers.map { x ->
        jsonData.values.remove(x)
    }

    mineableFile.toFile().writeText( gson.toJson(jsonData))

}