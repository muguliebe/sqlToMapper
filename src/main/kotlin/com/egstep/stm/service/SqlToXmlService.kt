package com.egstep.stm.service

import com.beust.klaxon.KlaxonException
import com.egstep.stm.base.Base
import com.egstep.stm.domain.Sql
import com.egstep.stm.fwk.RunMode
import com.egstep.stm.routine.Progress
import com.egstep.stm.util.FileUtils
import com.egstep.stm.util.SqlFileUtil
import org.dom4j.*
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.dom4j.tree.DefaultDocumentType
import org.xml.sax.SAXParseException
import java.io.File
import java.io.FileWriter
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess


object SqlToXmlService : Base() {

    private var pathSource: Path = Paths.get("")
        set(path) {
            field = path
            pathTarget = Paths.get(path.toAbsolutePath().toString(), "#result")
            println("working directory is: ${pathSource.toAbsolutePath()}")
        }
    private var pathTarget: Path = Paths.get("")
        set(path) {
            field = path
            println("\ntarget  directory is: ${pathTarget.toAbsolutePath()}")
        }
    private var sqlFiles: List<File> = listOf()
        set(files) {
            field = files
            if (sqlFiles.isEmpty()) {
                log.error("there is no sql files in ${pathSource.toAbsolutePath()}")
            } else {
                println("found ${files.size} sql files")
            }
        }

    fun progress() {

        displayComment()

        // set path(where exist sql files and target) by user
        pathSource = getInputPathByUser()

        // get Files, only sql
        sqlFiles = FileUtils.getFiles(pathSource).filter { it.extension == "sql" }

        // parsing sql file format
        println("\nSql Parsing Start")
        val sqls = mutableListOf<Sql>()
        for (file in sqlFiles) {
            try {
                val sql = SqlFileUtil.parse(file)
                if (sql.isValid) {
                    sqls.add(sql)
                }
            }catch (e : KlaxonException) {
                println("Parsing Error: ${e.message} in ${file}")
            }

        }
        println("Sql Parsing End")

        // write xml
        for (sql in sqls) {
            val document = getDocument(sql)
            if (document != null) {
                writeXml(document, sql)
            }
        }

        println("write in $pathTarget")
    }

    private fun displayComment() {
        Progress.displayStartLine()
        println("Enter the Path where in sql files" +
                "ex) /tmp/working/sql or just enter for ${FileSystems.getDefault().getPath(".").toAbsolutePath()}")
        print(">")
    }

    private fun getInputPathByUser(): Path {
        return if (commons.runMode == RunMode.DEBUG) {
            Paths.get("src/test/resources/sqlFiles")
        } else {
            val input = Scanner(System.`in`)
            val inPath = input.nextLine()
            Paths.get(inPath)   // FIXME | make acceptable relative path | ref | FileSystems.getDefault().getPath(".", name)
        }
    }

    private fun getDocument(sql: Sql): Document? {
        val path = Paths.get(pathTarget.toAbsolutePath().toString(), sql.mapperFileName + ".xml").toString()
        val xmlFile = File(path)

        val document = if (xmlFile.exists()) {
            try {
                SAXReader().read(xmlFile)
            } catch (e: Exception) {
                DocumentHelper.createDocument()
            }
        } else {
            DocumentHelper.createDocument()
        }

        if (document.rootElement == null) {
            document.addElement("mapper").addAttribute("namespace", sql.namespace)
        }

        // docType
        if (document.docType == null) {
            val docType: DocumentType = DefaultDocumentType()
            docType.elementName = "mapper"
            docType.publicID = "-//mybatis.org//DTD Mapper 3.0//EN"
            docType.systemID = "http://mybatis.org/dtd/mybatis-3-mapper.dtd"

            document.docType = docType
        }

        // validation
        val existElement = document.rootElement.elements()
                .asSequence()
                .filter { it.name.toLowerCase() == sql.queryType.toLowerCase() }
                .filter { it.attribute("id")?.value?.toLowerCase() == sql.id.toLowerCase() }
                .firstOrNull()

        // add new Element
        val newElement = if (existElement != null) {
            existElement.text = ""
            existElement
        } else {
            document.rootElement.addElement(sql.queryType)
        }
        newElement.addAttribute("id", sql.id)
                .addAttribute(QName.get("space", Namespace.XML_NAMESPACE), "preserve")
                .addText(sql.query)

        if (sql.parameterType != null) newElement.addAttribute("parameterType", sql.parameterType)
        if (sql.resultType != null) newElement.addAttribute("resultType", sql.resultType)

        return document
    }

    private fun writeXml(document: Document, sql: Sql) {
        val targetFilePath = Paths.get(pathTarget.toAbsolutePath().toString(), sql.mapperFileName + ".xml")
        if (targetFilePath.parent != null) {
            Files.createDirectories(targetFilePath.parent)
        }

        val fileWriter = FileWriter(targetFilePath.toAbsolutePath().toString())
        val format = OutputFormat.createPrettyPrint()
        val writer = XMLWriter(fileWriter, format)
        writer.write(document)
        writer.close()
    }
}

