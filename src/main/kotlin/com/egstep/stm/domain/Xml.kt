package com.egstep.stm.domain

data class Xml(
        val mapperFileName: String
) {
    val sqls : List<Sql> = emptyList()
}
