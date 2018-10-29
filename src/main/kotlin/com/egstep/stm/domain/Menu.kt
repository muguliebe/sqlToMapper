package com.egstep.stm.domain

data class Menu(
        val idx: Int,
        val name: String,
        val desc: String
) {
    companion object {
        val sqlToXml = Menu(idx = 1, name = "sqlToXml", desc = "sql file to mybatis mapper file")
//        val xmlToSql = Menu(idx = 2, name = "xmlToSql", desc = "xml file to csv file")
        val exit = Menu(idx = 0, name = "exit", desc = "bye")
//        val all: List<Menu> = listOf(sqlToXml, xmlToSql, exit)
        val all: List<Menu> = listOf(sqlToXml, exit)
    }
}
