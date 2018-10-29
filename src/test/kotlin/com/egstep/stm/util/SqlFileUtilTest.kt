package com.egstep.stm.util

import org.junit.Test
import java.io.File
import java.nio.file.Paths

class SqlFileUtilTest {

    @Test
    fun parse() {
        val filePath = Paths.get("src/test/resources/sqlFiles/test.sql").toAbsolutePath().toString()
        SqlFileUtil.parse(File(filePath))
    }


}
