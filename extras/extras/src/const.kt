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

val ORIG_METALS = listOf(
    "bismuth", "bismuth_bronze", "black_bronze", "bronze", "brass",
    "copper", "gold", "nickel", "rose_gold", "silver", "tin", "zinc",
    "sterling_silver", "wrought_iron", "cast_iron", "pig_iron", "steel",
    "black_steel", "blue_steel", "red_steel", "weak_steel", "weak_blue_steel",
    "weak_red_steel", "high_carbon_steel", "high_carbon_black_steel",
    "high_carbon_blue_steel", "high_carbon_red_steel"
)

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
    "glauconite",
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