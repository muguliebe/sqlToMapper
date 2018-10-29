package com.egstep.stm

import com.egstep.stm.domain.Menu
import com.egstep.stm.routine.Progress
import com.egstep.stm.service.SqlToXmlService
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    while (true) {
        val menuKey = Progress.displayMainMenu()

        when (menuKey) {
            Menu.sqlToXml -> SqlToXmlService.progress()
            Menu.exit     -> exitProcess(0)
            else          -> println("there is no matched menu")
        }
    }

}

