import java.io.File

fun main() {
    createGregHeatingItems()
}

data class HeatingItem(
    val ingredient: String,
    val fluid: String,
    val amount: Int,
    val temperature: Int
)

fun heating(ingredient: String, fluid: String, amount: Int, temperature: Int): HeatingItem {
    return HeatingItem(ingredient, fluid, amount , temperature)
}

val data = listOf(
    heating("gtceu:bronze_turbine_blade", "gtceu:bronze", 1,  950 ),
    heating("gtceu:bronze_sword_head", "gtceu:bronze", 200,  950 ),
    heating("gtceu:bronze_small_gear", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_shovel_head", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_scythe_head", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_screw", "gtceu:bronze", 11,  950 ),
    heating("gtceu:bronze_saw_head", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_rotor", "gtceu:bronze", 400,  950 ),
    heating("gtceu:bronze_rod", "gtceu:bronze", 50,  950 ),
    heating("gtceu:bronze_ring", "gtceu:bronze", 25,  950 ),
    heating("gtceu:bronze_plate", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_pickaxe_head", "gtceu:bronze", 100,  950 ),
    heating("gtceu:bronze_nugget", "gtceu:bronze", 11,  950 ),
    heating("gtceu:bronze_mining_hammer_head", "gtceu:bronze", 600 , 950 ),
    heating("gtceu:bronze_long_rod", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_knife_head", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_knife_butchery_head", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_ingot", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_hoe_head", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_hammer_head", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_gear", "gtceu:bronze", 400 , 950 ),
    heating("gtceu:bronze_foil", "gtceu:bronze", 25 , 950 ),
    heating("gtceu:bronze_file_head", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_dust", "gtceu:bronze", 100 , 950 ),
    heating("gtceu:bronze_double_plate", "gtceu:bronze", 200 , 950 ),
    heating("gtceu:bronze_bolt", "gtceu:bronze", 12 , 950 ),
    heating("gtceu:bronze_axe_head", "gtceu:bronze", 100 , 950 ),

    heating("gtceu:copper_sword_head", "gtceu:copper", 200,  1080 ),
    heating("gtceu:copper_spring", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_small_spring", "gtceu:copper", 25,  1080 ),
    heating("gtceu:copper_shovel_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_scythe_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_screw", "gtceu:copper", 11,  1080 ),
    heating("gtceu:copper_saw_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_rod", "gtceu:copper", 50,  1080 ),
    heating("gtceu:copper_pickaxe_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_nugget", "gtceu:copper", 11,  1080 ),
    heating("gtceu:copper_mining_hammer_head", "gtceu:copper", 600,  1080 ),
    heating("gtceu:copper_long_rod", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_knife_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_knife_butchery_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_ingot", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_hoe_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_hammer_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_foil", "gtceu:copper", 25,  1080 ),
    heating("gtceu:copper_fine_wire", "gtceu:copper", 12,  1080 ),
    heating("gtceu:copper_file_head", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_dust", "gtceu:copper", 100,  1080 ),
    heating("gtceu:copper_double_plate", "gtceu:copper", 200,  1080 ),
    heating("gtceu:copper_bolt", "gtceu:copper", 12,  1080 ),
    heating("gtceu:copper_axe_head", "gtceu:copper", 100,  1080 ),




)


private val recipiesTypes = listOf(
     "heating"
)

// create TFC heating recipies for greg Items
fun createGregHeatingItems() {
    val recipyPath = destinationFolder.path + "\\data\\tfc\\recipes\\heating\\"
    File(recipyPath).mkdirs()

    data.map {

        val json = it.generateJson()

        File(recipyPath + "\\" + it.ingredient.split(":").last() + ".json").writeText(json)
    }

}

fun HeatingItem.generateJson(): String {

    val json = pattren
        .replace("$1", ingredient)
        .replace("$2", fluid)
        .replace("$3", amount.toString())
        .replace("$4", temperature.toString())
    return json
}


private val pattren = """
    {
      "__comment__": "This file was automatically created by mcresources",
      "type": "tfc:heating",
      "ingredient": {
        "item": "$1"
      },
      "result_fluid": {
        "fluid": "$2",
        "amount": $3
      },
      "temperature": $4
    }
""".trimIndent()