package com.egstep.stm

import org.junit.Test
import org.w3c.dom.Document
import java.io.File
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class WhatTest {
    @Test
    fun test() {
        val f = File("/Users/zany/tmp/yoi.xml")
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isValidating = false

        val document = documentBuilderFactory.newDocumentBuilder().parse(f)
        document.documentElement.normalize()

        val nodeList = document.getElementsByTagName("mapper")
        val nodeMapper = nodeList.item(0)
        val namespace = nodeMapper.attributes.getNamedItem("namespace")?.nodeValue

        val source = DOMSource(document)
        val writer = Files.newBufferedWriter(
                FileSystems.getDefault().getPath("/Users/zany/tmp/yo.xml"),
                Charset.forName("UTF-8"),
                StandardOpenOption.CREATE)

        val result = StreamResult(writer)

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val domImpl = document.implementation
        val doctype = domImpl.createDocumentType("doctype",
                "-//mybatis.org//DTD Mapper 3.0//EN",
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd")
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.publicId)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.systemId)
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")

        transformer.transform(source, result)
    }

    @Test
    fun testtest() {
        val f = File("/Users/zany/tmp/yoi.xml")
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isValidating = false

        val document: Document = documentBuilderFactory.newDocumentBuilder().newDocument()
        if (f.exists()) {
            val document = documentBuilderFactory.newDocumentBuilder().parse(f)
            document.documentElement.normalize()
            val nodeList = document.getElementsByTagName("mapper")
            val nodeMapper = nodeList.item(0)
            val namespace = nodeMapper.attributes.getNamedItem("namespace")?.nodeValue
        } else {

        }

        val source = DOMSource(document)
        val writer = Files.newBufferedWriter(
                FileSystems.getDefault().getPath("/Users/zany/tmp/yo.xml"),
                Charset.forName("UTF-8"),
                StandardOpenOption.CREATE)

        val result = StreamResult(writer)

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val domImpl = document.implementation
        val doctype = domImpl.createDocumentType("doctype",
                "-//mybatis.org//DTD Mapper 3.0//EN",
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd")
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.publicId)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.systemId)
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")

        transformer.transform(source, result)
    }
}
