package com.egstep.stm.util

import com.beust.klaxon.Klaxon
import com.egstep.stm.domain.Sql
import java.io.File

object SqlFileUtil {
    fun parse(file: File): Sql {

        // =============================================================================
        // read file & split comment and query string
        val lines = file.bufferedReader().readLines()
        val sb = StringBuilder()
        val query = StringBuilder()
        var bGetComment = false
        for (line in lines) {
            if (!bGetComment) {
                if (line.trim().contains("*/")) {
                    sb.append(line.substring(2 % line.length, line.length))
                    bGetComment = true
                }
                sb.append(line)
            } else {
                query.append(line)
            }
        }

        // =============================================================================
        // remove comment syntax
        var json = sb.toString().replace("/*", "")
        json = json.replace("*/", "").trim()


        // =============================================================================
        // convert to class
        val sql = Klaxon().parse<Sql>(json)!!
        sql.sqlFileName = file.absolutePath
        sql.query = query.toString()

        // =============================================================================
        // validation
        if (!sql.isValid) {
            println("Parsing Error: '${sql.requiredProperty}' is necessary in ${sql.sqlFileName}")
        } else {
            println("${sql.sqlFileName} parse ..")
        }

        // =============================================================================
        // end of process
        return sql
    }
}
