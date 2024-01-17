import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.FileVisitResult.CONTINUE
import java.nio.file.attribute.BasicFileAttributes

fun main() {
    replaceAnvilRecipies()
}

private val recipiesTypes = listOf(
    //"anvil", "welding"
    "anvil"
)


fun replaceAnvilRecipies() {

    // new metals
    recipiesTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")
        val srcRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")

        process(destRecipiesPath, srcRecipiesPath)
    }

    // tfc metals
    recipiesTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")
        val srcRecipiesPath = Path.of(sourceFolder.path + "\\data\\tfc\\recipes\\$type")

        process(destRecipiesPath, srcRecipiesPath)
    }


}

private fun process( destFolder : Path, srcFolder: Path) {

    val fileVisitor = object : FileVisitor<Path> {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
            return CONTINUE
        }

        override fun visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult {
            val json = path.toFile().readText()

            val file = path.toFile();
            if (file.extension != "json") {
                return CONTINUE
            }

            val destFile = (destFolder.toFile().absolutePath + "\\${path.toFile().name}").run {
                File(this)
            }

            (ORIG_METALS + METALS)
                .firstOrNull { metal ->
                    json.contains("/$metal\"")
                }
                ?.let { metal ->
                    val newJson = json
                        .replaceFirst("tag\": \"forge:sheets/$metal\"", "item\": \"tfc:metal/sheet/$metal\"")
                        .replaceFirst("tag\": \"forge:double_sheets/$metal\"", "item\": \"tfc:metal/double_sheet/$metal\"")

                        .replaceFirst("tag\": \"forge:sheets/$metal\"", "item\": \"tfc:metal/sheet/$metal\"")
                        .replaceFirst("tag\": \"forge:double_sheets/$metal\"", "item\": \"tfc:metal/double_sheet/$metal\"")

                    if(newJson != json){
                        destFile.writeText(newJson)
                    }
                }


            return CONTINUE
        }

        override fun visitFileFailed(file: Path, exc: IOException): FileVisitResult {
            return CONTINUE
        }

        override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
            return CONTINUE
        }
    }

    Files.walkFileTree(srcFolder, setOf(FileVisitOption.FOLLOW_LINKS), Int.MAX_VALUE, fileVisitor)
    println("Files containing specified words have been copied to the destination folder.")
}


