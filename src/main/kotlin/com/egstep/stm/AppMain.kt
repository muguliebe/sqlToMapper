package com.egstep.stm

import org.slf4j.LoggerFactory

class AppMain
fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(AppMain::class.java)

    Prompt.displayBasic()
    Prompt().test()
}


