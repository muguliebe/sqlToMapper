package com.egstep.stm

import com.egstep.stm.base.Base
import com.egstep.stm.domain.Menu
import com.egstep.stm.fwk.RunMode
import com.egstep.stm.routine.Progress
import com.egstep.stm.service.SqlToXmlService
import com.egstep.stm.service.XmlToSqlService
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

class AppMain

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(AppMain::class.java)

    while (true) {
        val menuKey = Progress.displayMainMenu()

//        Base.commons.runMode = RunMode.DEBUG

        when (menuKey) {
            Menu.sqlToXml -> SqlToXmlService.progress()
//            Menu.xmlToSql -> XmlToSqlService.progress()
            Menu.exit     -> exitProcess(0)
            else          -> println("there is no matched menu")
        }
    }

}

