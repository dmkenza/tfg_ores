import java.io.File

fun main() {
    createGregRecipies()
}


private val pairsNewOres = listOf(
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


val dataOldOres = listOf(
    "malachite" to "gtceu:malachite_crushed_ore",
    "bismuthinite" to "gtceu:bismuth_crushed_ore",
    "native_copper" to "gtceu:copper_crushed_ore",
    "cassiterite" to "gtceu:cassiterite_crushed_ore",
    "native_silver" to "gtceu:silver_crushed_ore",
    "hematite" to "gtceu:hematite_crushed_ore",
    "native_gold" to "gtceu:gold_crushed_ore",
    "magnetite" to "gtceu:magnetite_crushed_ore",
    "garnierite" to "gtceu:garnierite_crushed_ore",
    "limonite" to "gtceu:yellow_limonite_crushed_ore",
    "sphalerite" to "gtceu:sphalerite_crushed_ore",
    "tetrahedrite" to "gtceu:tetrahedrite_crushed_ore"
)

val dataOldMinerals = listOf(
    "cinnabar" to "minecraft:redstone" and "8",
    "cryolite" to "minecraft:redstone" and "8",
    "sulfur" to "tfc:powder/sulfur" and "4",
    "graphite" to "tfc:powder/graphite" and "4",
    "pyrite" to "tfc:powder/pyrite" and "4",
    "saltpeter" to "tfc:powder/saltpeter" and "4",
    "sylvite" to "tfc:powder/sylvite" and "4",
    "borax" to "tfc:powder/flux" and "6",
    "halite" to "tfc:powder/salt" and "4",
    "diamond" to "tfc:powder/diamond" and "4"
)

private infix fun <A, B, C> Pair<A, B>.and(c: C): Triple<A, B, C> {
    return Triple(this.first, this.second, c)
}

val GRADES = listOf(
    "small" to "9", "poor" to "5", "normal" to "3", "rich" to "2"
)

fun createGregRecipies() {

    GRADES.flatMap { (grade, oreCount) ->
        pairsNewOres.map { (ore, dust) ->
            writeMaceratorRecipe(
                grade + "_",
                ore,
                oreCount,
                dust
            )
        }
    }

    GRADES.flatMap { (grade, oreCount) ->
        dataOldOres.map { (ore, crushedOre) ->
            writeMaceratorRecipe(
                grade + "_",
                ore,
                oreCount,
                null,
                crushedOre
            )
        }
    }

    dataOldMinerals.map { (ore, crushedOre, amountResult) ->
        writeMaceratorRecipe(
            "",
            ore,
            "1",
            null,
            crushedOre,
            amountResult
        )
    }


}

private fun writeMaceratorRecipe(
    prefix: String,
    ore: String,
    oreCount: String,
    dust: String? = null,
    crushedOre: String? = null,
    countResult: String? = null
) {
    val maceratorRecipiesPath = destinationFolder.path + "\\data\\gtceu\\recipes\\macerator\\"

    File(maceratorRecipiesPath).mkdirs()

//    val prefix = "poor_"
//    val ore = "galena"
//    val dust = "minecraft:redstone_dust"

    val filename = "$prefix$ore"

//    val oreCount = "10" //1
    val oreItem = "tfc:ore/$prefix$ore" //2
    val resultItem =
        if (crushedOre == null) {
            "gtceu:${ore}_crushed_ore"
        } else {
            crushedOre
        }


    val resultDustItem =
        if (dust == null)
            ""
        else if (dust.contains(":")) {
            dust
        } else {
            "gtceu:${dust}_dust"
        }

    val pattern = if (countResult != null) {
        template_meneral
    } else if (resultDustItem.isEmpty()) {
        template_empty_dust
    } else {
        template
    }

    val json = pattern
        .replace("$1", oreCount)
        .replace("$2", oreItem)
        .replace("$3", resultItem)
        .replace("$4", resultDustItem)
        .replace("$5", countResult.orEmpty())

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


private val template_meneral = """ 
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
                   "count":$5,
                   "ingredient":{
                      "item":"$3"
                   }
                },
                "chance":1.0,
                "tierChanceBoost":0.0
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


private val template_empty_dust = """ 
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