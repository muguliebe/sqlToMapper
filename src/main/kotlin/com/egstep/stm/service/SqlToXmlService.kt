package com.egstep.stm.service

import com.egstep.stm.base.Base
import com.egstep.stm.domain.Sql
import com.egstep.stm.fwk.RunMode
import com.egstep.stm.routine.Progress
import com.egstep.stm.util.FileUtils
import com.egstep.stm.util.SqlFileUtil
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.io.StringWriter
import java.nio.charset.Charset
import java.nio.file.*
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import com.sun.org.apache.xml.internal.serialize.OutputFormat



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
        val sqls = sqlFiles.map { SqlFileUtil.parse(it) }
                .filter { it.isValid }
                .toMutableList()
        println("Sql Parsing End")

        // write xml
        sqls.stream()
                .map { getDocument(it) } // Sql -> Document
                .forEach { writeXml(it) }
    }

    private fun displayComment() {
        Progress.displayStartLine()
        println("Enter the Path where in sql files ex) /tmp/working/sql")
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

    private fun getDocument(sql: Sql): Pair<Document, Sql> {
        val path = Paths.get(pathTarget.toAbsolutePath().toString(), sql.mapperFileName + ".xml").toString()
        val xmlFile = File(path)

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isValidating = false

        var document: Document = documentBuilderFactory.newDocumentBuilder().newDocument()

        if (xmlFile.exists()) {
            document = documentBuilderFactory.newDocumentBuilder().parse(xmlFile)
            val nodeList = document.getElementsByTagName("mapper")
            val nodeMapper = nodeList.item(0)
            nodeMapper.appendChild(createQueryElement(document, sql))

            // todo | validation namespace name is diff with input sql
//            val namespace = nodeMapper.attributes.getNamedItem("namespace")?.nodeValue

        } else {
            val nodeMapper = document.createElement("mapper")
            nodeMapper.setAttribute("namespace", sql.namespace)
            document.appendChild(nodeMapper)

            nodeMapper.appendChild(createQueryElement(document, sql))
        }

        return Pair(document, sql)
    }

    private fun createQueryElement(document: Document, sql: Sql): Element {
        val addChild = document.createElement(sql.queryType)
        addChild.setAttribute("id", sql.id)
//        addChild.textContent = sql.query
        addChild.appendChild(document.createTextNode(sql.query))

        if (sql.parameterType != null) addChild.setAttribute("parameterType", sql.parameterType)
        if (sql.resultType != null) addChild.setAttribute("resultType", sql.resultType)

        return addChild
    }

    private fun writeXml(pair: Pair<Document, Sql>) {
        val document = pair.first
        val sql = pair.second

        val targetFilePath = Paths.get(pathTarget.toAbsolutePath().toString(), sql.mapperFileName + ".xml")
        if (targetFilePath.parent != null) {
            Files.createDirectories(targetFilePath.parent)
        }

        val source = DOMSource(document)
        val writer = Files.newBufferedWriter(
                FileSystems.getDefault().getPath(targetFilePath.toAbsolutePath().toString()),
                Charset.forName("UTF-8"),
                StandardOpenOption.CREATE)
//        val writer = StringWriter()

        val result = StreamResult(writer)

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val domImpl = document.implementation
        val doctype = domImpl.createDocumentType("doctype", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd")
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.publicId)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.systemId)
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")

        transformer.transform(source, result)

        println("Xml Write: $targetFilePath")
    }

}
