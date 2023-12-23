import com.google.gson.GsonBuilder
import java.nio.file.Paths


var gson = GsonBuilder().setPrettyPrinting().create()

val sourceFolder = Paths.get("C:\\projects_mc\\work\\terrafirmagreg\\src\\main\\resources").toFile()
val destinationFolder = Paths.get("C:\\projects_mc\\work\\tfg_ores\\src\\main\\generated").toFile()

val leftovers = listOf(
    //"C:\\projects_mc\\work\\tfg_ores\\src\\main\\generated\\data\\tfc\\tags\\blocks\\rock\\ores.json"
    "C:\\projects_mc\\work\\tfg_ores\\src\\main\\generated\\data"
)

//C:\projects_mc\work\tfg_ores\src\main\generated\data\tfc\recipes\collapse
//C:\projects_mc\work\tfg_ores\src\main\generated\data\tfc\tags

fun getKeys(): List<String> {
    return METALS + ORES
}

val METALS = listOf(
    "lead"
)

val ORES = listOf(
    "galena",
    "stibnite",
    "zeolite",
    "cobaltite",
    "vanadium_magnetite",
    "pentlandite",
    "goethite",
    "bastnasit",
    "tantalite",
    "calcite",
    "lepidolite",
    "bastnasite",
    "pyrolusite",
    "barite",
    "bentonite",
    "tricalcium_phosphate",
    "asbestos",
    "pyrochlore",
    "mica",
    "kyanite",
    "alunite",
    "electrotine",
    "diatomite",
    "neodymium",
    "soapstone",
    "talc",
    "glaucosite",
    "lithium",
    "bornite",
    "platinum",
    "wulfenite",
    "molybdenum",
    "plutonium",
    "tungstate",
    "scheelite",
    "palladium",
    "powellite",
    "naquadah"
)