import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.file.Path


fun main() {
    processLang()
}

val langs = listOf("en_us.json")

fun processLang() {
    val destLangs = Path.of(destinationFolder.path + "\\assets\\tfc\\lang")
    val destLang = Path.of(destinationFolder.path + "\\assets\\tfc\\lang\\en_us.json")


    //clear unnecessary langs
    destLangs
        .toFile()
        .listFiles()
        ?.filter { file ->
            !langs.contains(file.name)
        }
        ?.forEach { file ->
            file.delete()
        }


    val json = destLang.toFile().readText()

    val type: Type = object : TypeToken<Map<String?, String?>?>() {}.getType()
    val map: Map<String, String> = gson.fromJson(json, type)

    //clear TFC lang entities
    val entries = map.entries.filter { (key, value) ->

        ORES.firstOrNull { ore ->
            key.contains(ore)
        } != null
                ||
                METALS.firstOrNull { ore ->
                    key.contains(ore)
                } != null
    }

    val newMap =
        entries
            .groupBy { it.key }
            .mapValues { entry -> entry.value.map { it.value }.firstOrNull() }

    val newJson = gson.toJson(newMap)

    if (newJson == "[]") {
        throw Exception("no text")
    }

    destLang.toFile().writeText(newJson)
}