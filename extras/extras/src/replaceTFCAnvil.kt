import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.FileVisitResult.CONTINUE
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension

fun main() {
    createGregCasting()
}

private val recipiesTypes = listOf(
    "anvil"
)

//private val secondsTypes = listOf(
//    "metals"
//)

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
            val content = path.toFile().readText()

            val file = path.toFile();
            if (file.extension != "json") {
                return CONTINUE
            }

            val destFile = (destFolder.toFile().absolutePath + "\\${path.toFile().name}").run {
                File(this)
            }

            (ORIG_METALS + METALS)
                .filter { metal ->
                    content.contains("tfc:metal/$metal")
                }
                .forEach { metal ->
                    val json = content
                        .replaceFirst("fluid\": \"tfc:metal/$metal\"", "fluid\": \"gtceu:$metal\"")
                        .replaceFirst("ingredient\": \"tfc:metal/$metal\"", "ingredient\": \"gtceu:$metal\"")

                    destFile.writeText(json)
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


