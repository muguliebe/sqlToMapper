package com.egstep.stm.util

import java.io.File
import java.nio.file.Path


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
