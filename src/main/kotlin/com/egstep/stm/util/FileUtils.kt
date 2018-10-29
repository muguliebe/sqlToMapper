package com.egstep.stm.util

import java.io.File
import java.nio.file.Path
import java.io.IOException
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.util.ArrayList
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.io.BufferedReader



class FileUtils {
    companion object {
        fun getFiles(path: String) = File(path)
                .walkBottomUp()
                .sortedBy { it.isDirectory }
                .filter { it.isFile }
                .toList()

        fun getFiles(path: Path): List<File> = getFiles(path.toAbsolutePath().toString()).toList()

        fun readFileLines(fileName: String): List<String> = File(fileName).bufferedReader().readLines()

    }
}
