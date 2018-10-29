package com.egstep.stm.routine

import com.egstep.stm.base.Base
import com.egstep.stm.domain.Menu
import java.util.*

class Progress : Base() {

    companion object {
        init {
            // start signature
            (1..50).forEach { print("=") }
            println()
            println("s q l  2  m a p p e r")
            (1..50).forEach { print("=") }
            println()
        }

        fun displayStartLine() {
            println()
            (1..50).forEach { print("-") }
            println()
        }

        fun displayMainMenu(): Menu? {
            println("\nSelect the Menu ==================================")
            Menu.all.forEach {
                println("${it.idx}. ${it.name}: ${it.desc}")
            }
            print("choose the number >")

            val input = Scanner(System.`in`)
            val inIdx = input.nextInt()
            val menu = Menu.all.filter { it.idx == inIdx }
                    .firstOrNull()
            return menu
        }
    }
}

