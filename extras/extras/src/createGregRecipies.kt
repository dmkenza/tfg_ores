import java.io.File

private val pairs = listOf(
    "galena" to "sulfur",
    "stibnite" to "antimony_trioxide",
    "zeolite" to "calcuim",
    "cobaltite" to "sulfur",
    "vanadium_magnetite" to "magnetite",
    "pentlandite" to "iron",
    "goethite" to "iron", //malachite_gem
    "tantalite" to "manganese",
    "calcite" to "calcuim",
    "lepidolite" to "lithium",
    "bastnasite" to "neodymium",
    "pyrolusite" to "manganese",
    "barite" to "barite",
    "bentonite" to "aluminium",
    "tricalcium_phosphate" to "apatite",
    "asbestos" to "diatomite",
    "pyrochlore" to "apatite",
    "mica" to "potassium",
    "kyanite" to "talc",
    "alunite" to "alunite",
    "electrotine" to "minecraft:redstone_dust", // minecraft:redstone_dust
    "diatomite" to "hematite",
    "neodymium" to "rare_earth",
    "soapstone" to "silicon_dioxide",
    "talc" to "clay",
    "glauconite" to "sodium",
    "lithium" to "lithium",
    "bornite" to "pyrite",
    "platinum" to "nickel",
    "wulfenite" to "iron",
    "molybdenum" to "molybdenum",
    "plutonium" to "uraninite",
    "tungstate" to "manganese",
    "scheelite" to "manganese",
    "palladium" to "palladium",
    "powellite" to "iron",
    "naquadah" to "sulfur",
    "uraninite" to "uraninite",
    "pitchblende" to "thorium"

    //"bastnasit" to "neodymium",

)

val GRADES = listOf(
    "small" to "9", "poor" to "5", "normal" to "3", "rich" to "2"
)

fun createGregRecipies() {

    GRADES.flatMap { (grade, oreCount) ->
        pairs.map { (ore, dust) ->
            writeMaceratorRecipe(
                grade + "_",
                ore,
                oreCount,
                dust
            )
        }
    }

}

private fun writeMaceratorRecipe(
    prefix: String,
    ore: String,
    oreCount: String,
    dust: String
) {
    val maceratorRecipiesPath = destinationFolder.path + "\\data\\gtceu\\recipes\\macerator\\"

    File(maceratorRecipiesPath).mkdirs()

//    val prefix = "poor_"
//    val ore = "galena"
//    val dust = "minecraft:redstone_dust"

    val filename = "$prefix$ore"

//    val oreCount = "10" //1
    val oreItem = "tfc:ore/$prefix$ore" //2
    val resultItem = "gtceu:${ore}_crushed_ore"
    val resultDustItem =
        if (ore.contains(":")) {
            "minecraft:redstone_dust"
        } else {
            "gtceu:${dust}_dust"
        }

    val json = template
        .replace("$1", oreCount)
        .replace("$2", oreItem)
        .replace("$3", resultItem)
        .replace("$4", resultDustItem)

    File(maceratorRecipiesPath + "\\" + filename + ".json").writeText(json)
}


private val template = """ 
    {
       "type":"gtceu:macerator",
       "duration":400,
       "inputs":{
          "item":[
             {
                "content":{
                   "type":"gtceu:sized",
                   "fabric:type":"gtceu:sized",
                   "count":$1,
                   "ingredient":{
                      "item":"$2"
                   }
                },
                "chance":1.0,
                "tierChanceBoost":0.0
             }
          ]
       },
       "outputs":{
          "item":[
             {
                "content":{
                   "type":"gtceu:sized",
                   "fabric:type":"gtceu:sized",
                   "count":1,
                   "ingredient":{
                      "item":"$3"
                   }
                },
                "chance":1.0,
                "tierChanceBoost":0.0
             },
             {
                "content":{
                   "type":"gtceu:sized",
                   "fabric:type":"gtceu:sized",
                   "count":1,
                   "ingredient":{
                      "item":"$3"
                   }
                },
                "chance":0.3,
                "tierChanceBoost":0.085
             },
             {
                "content":{
                   "type":"gtceu:sized",
                   "fabric:type":"gtceu:sized",
                   "count":1,
                   "ingredient":{
                      "item":"$4"
                   }
                },
                "chance":0.14,
                "tierChanceBoost":0.085
             }
          ]
       },
       "tickInputs":{
          "eu":[
             {
                "content":2,
                "chance":1.0,
                "tierChanceBoost":0.0
             }
          ]
       },
       "tickOutputs":{
          
       }
    }
""".trimIndent()