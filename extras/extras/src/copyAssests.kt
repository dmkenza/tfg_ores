import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.FileVisitResult.CONTINUE
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension
import kotlin.io.path.name

fun deleteRecursive(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory()) for (child in fileOrDirectory.listFiles()) deleteRecursive(child)
    fileOrDirectory.delete()
}

fun copyAssests() {
//    val sourceFolder = File("path/to/source/folder")
//    val destinationFolder = File("path/to/destination/folder")
//    val wordsToSearch = listOf("word1", "word2", "word3") // Add your list of words here

//    File("C:\\projects_mc\\work\\tfg_ores\\src\\main\\resources\\assets\\tfc\\textures\\block\\ore").mkdirs()
//    return

    if (!sourceFolder.exists() || !sourceFolder.isDirectory) {
        println("Source folder does not exist or is not a directory.")
        return
    }

    deleteRecursive(destinationFolder)

    if (!destinationFolder.exists()) {
        destinationFolder.mkdirs()
    }

    val fileVisitor = object : FileVisitor<Path> {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
            return CONTINUE
        }

        override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
            val content = file.toFile().readText()

            if(file.toFile().extension != "json"){
                return CONTINUE
            }

            if (getKeys().any {
                        word -> content.contains(word) ||
                        file.toFile().name.contains(word)
            }) {
                val relativePath = sourceFolder.toPath().relativize(file)
                val destinationFile = destinationFolder.toPath().resolve(relativePath).toFile()
                destinationFile.parentFile.mkdirs()
                file.toFile().copyTo(destinationFile, true)
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

    Files.walkFileTree(sourceFolder.toPath(), setOf(FileVisitOption.FOLLOW_LINKS), Int.MAX_VALUE, fileVisitor)
    println("Files containing specified words have been copied to the destination folder.")
}
