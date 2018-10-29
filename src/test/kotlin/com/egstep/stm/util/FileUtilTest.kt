package com.egstep.stm.util

import org.junit.Test

class FileUtilTest {

    @Test
    fun getFilesTest() {
        FileUtils.getFiles("/log")
    }
}
