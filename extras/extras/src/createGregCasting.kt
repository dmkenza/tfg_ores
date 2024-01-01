import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.FileVisitResult.CONTINUE
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension

fun main() {
    createGregCasting()
}

private fun deleteRecursive(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory()) for (child in fileOrDirectory.listFiles()) deleteRecursive(child)
    fileOrDirectory.delete()
}

private val recipiesTypes = listOf(
    "heating\\metal", "casting" , "heating\\ore", "heating"
)

private val secondsTypes = listOf(
    "metals"
)

fun createGregCasting() {

    // new metals
    recipiesTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")
        val srcRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")

        process(destRecipiesPath, srcRecipiesPath)
    }

    secondsTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\tfc\\$type")
        val srcRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\tfc\\$type")

        process(destRecipiesPath, srcRecipiesPath)
    }


    // tfc metals
    recipiesTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\recipes\\$type")
        val srcRecipiesPath = Path.of(sourceFolder.path + "\\data\\tfc\\recipes\\$type")

        process(destRecipiesPath, srcRecipiesPath)
    }

    secondsTypes.forEach { type ->
        val destRecipiesPath = Path.of(destinationFolder.path + "\\data\\tfc\\tfc\\$type")
        val srcRecipiesPath = Path.of(sourceFolder.path + "\\data\\tfc\\tfc\\$type")

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


fun String.replaceFirstAftere(afterWord: String, old: String, new: String): String {

    // Find the index of the word in the original string
    val indexOfWord = this.indexOf(afterWord)

    // If the word is found, replace the first occurrence of "Hello" after that index
    return if (indexOfWord != -1) {
        this.substring(0, indexOfWord + afterWord.length) +
                this.substring(indexOfWord + afterWord.length)
                    .replaceFirst(old, new)
    } else {
        this
    }
}

// Extension function to replace the last occurrence of a substring in a string
fun String.replaceLast(oldValue: String, newValue: String): String {
    val lastIndex = lastIndexOf(oldValue)
    return if (lastIndex != -1) {
        substring(0, lastIndex) + newValue + substring(lastIndex + oldValue.length)
    } else {
        this
    }
}