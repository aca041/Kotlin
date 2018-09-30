import java.io.File
import java.security.MessageDigest
import java.math.BigInteger

import java.io.FileWriter
import java.io.IOException
import java.nio.file.*
import java.nio.file.Files.getLastModifiedTime

private const val CSV_HEADER = "File|MD5Sum"


fun main (args: Array<String>){


    /*val path = Paths.get(if(args.isEmpty()){

        System.getProperty("C:\\Temp\\sd card\\")
    } else {
        args[0]
    })*/
    //val path = Paths.get(URI.create("file:///Temp/"))
    //C:\Temp\sd card
    //val path = FileSystems.getDefault().getPath("/Temp/sd card")

    //val list = List<File>.duplicates()

   // println("list.isEmpty() is ${list.isEmpty()}") // true



    var fileWriter: FileWriter? = null

    try {
//C:\Users\u6063677\Downloads

        fileWriter = FileWriter("/Temp/DuplicateList-Download.csv")
        fileWriter.append(CSV_HEADER)
        fileWriter.append('\n')
        //File("C:\\user\\u6063677\\Downloads").listDuplicates(fileWriter)
        File("C:\\Temp\\pictures").listDuplicates(fileWriter)
        //"C:\\Users\\u6063677\\OneDrive - Thomson Reuters Incorporated"
//C:\Users\u6063677\Downloads
        /*
        fileWriter = FileWriter("/Temp/DuplicateList-Download.csv")
        fileWriter.append(CSV_HEADER)
        fileWriter.append('\n')
        File("C:\\Temp\\duplicate temp folder").listDuplicates1().
*/
/*
        fileWriter.append("AllFiles")
        fileWriter.append('\n')

        File("C:\\Temp\\duplicate temp folder").allFilesIn().forEach {
            fileWriter.append(it.absolutePath + "|" + it.md5())
            fileWriter.append('\n')
        }
*/
        println("Write CSV successfully!")

    } catch (e: Exception) {
        println("Writing CSV error!")
        e.printStackTrace()
    } finally {
        try {
            fileWriter!!.flush()
            fileWriter.close()
        } catch (e: IOException) {
            println("Flushing/closing error!")
            e.printStackTrace()
        }
    }
    //fileWriter.append(CSV_HEADER)
    //fileWriter.append('\n')
/*
    File("/Temp/abc").listDuplicates().toString().toMutableList().forEach {
        println(it.toString())
    }
*/

    /* File("/Temp/sd card").deleteDuplicates()
    println(File("C:\\Temp\\sd card\\Download\\20170422 SAP Baler with Fer\\20170422 (276).JPG").md5())
    println(File("C:\\Temp\\sd card\\Download\\20170422 SAP Baler with Fer\\20170422 (277).JPG").md5())
    println(File("C:\\Temp\\sd card\\Download\\20170422 SAP Baler with Fer\\20170422 (278).JPG").md5())

    */

    /*File("/Temp/sd card").md5().forEach {
        println(it)
    }*/
    /*File("/Temp/sd card").walk().forEach {
        println(it)
    }*/

    /*
    File("/Temp/sd card").walk().forEach {
        println(it)
    }
*/

/*
    if (Files.isDirectory(path)){
        //List all items in the directory. Note that we are using Java 8 streaming API to group the entries by
        //directory and files
        val fileDirMap = Files.list(path).collect(Collectors.partitioningBy( {it -> Files.isDirectory(it)}))

        println("Directories")
        //Print out all of the directories
        fileDirMap[true]?.forEach { it -> println(it.fileName) }




        println("\nFiles")
        println("%-20s\tRead\tWrite\tExecute".format("Name"))
        //Print out all files and attributes
        fileDirMap[false]?.forEach( {it ->
            println("%-20s\t%-5b\t%-5b\t%b".format(
                    it.fileName,
                    Files.isReadable(it), //Read attribute
                    Files.isWritable(it), //Write attribute
                    Files.isExecutable(it))) //Execute attribute
        })
    } else {
        println("Enter a directory")
    }
*/
}



/** list all duplicate files at or under this one */
fun File.listDuplicates(fileWriter: FileWriter? = null): Unit =
        listOf(element = this).duplicates().forEach {
            fileWriter?.append(it.absolutePath + "|" + it.md5() +  "|" + '\n')

        }
//listOf(this).duplicates().forEach { it.delete() }

fun File.listDuplicates1(): Unit =
        listOf(element = this).duplicates().forEach {
            it.md5()
        }



/** Delete all duplicate files at or under this one */
fun File.deleteDuplicates(): Unit =
        listOf(this).duplicates().forEach { it.delete() }

/** Recursively search for duplicate files anywhere at or under files in this list */
private fun List<File>.duplicates(): List<File> =
        flatMap { it.allFilesIn() }.distinct()
                .sortedBy { it.nameWithoutExtension }
                // Find lists of files having the same size
                .groupBy { it.length() }.values.filter { it.size > 1 }
                // Group these files by md5 hash, ignoring the first
                .flatMap { it.groupBy { it.md5() }.values }
                .map { it.drop(0) }
                .flatten()

/** Return a list of all files at or under this one (recursively) */
private fun File.allFilesIn(): List<File> = when {
    isFile -> listOf(this)
    else -> listFiles().flatMap { it.allFilesIn() }
}

/** Return an MD5 hash of this file as a string */
private fun File.md5(): String = with(MessageDigest.getInstance("MD5")) {
    forEachBlock { buffer, bytesRead ->
        update(buffer, 0, bytesRead)
    }
    BigInteger(1, digest()).toString(16)
}