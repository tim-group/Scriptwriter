package com.youdevise.test.scriptwriter;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PrinterTest {
    private DocumentBuilder builder;
    private Document doc;
    private File outputDir;

    @Before public void
    setupDocument() throws Exception {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(new TrivialEntityResolver());
        doc = builder.newDocument();
        Element books = addChildElement(doc, "books");
        Element bigSleep = addChildElement(books, "book");
        addTextChild(bigSleep, "The Big Sleep");
    }

    @Before public void
    createOutputDirectory() throws Exception {
        outputDir = makeTempDir("Printer-test-output");
    }
    
    @Test public void 
    createsAFileInOutputDir() throws Exception {
        runPrinter();
        assertThat(outputDir.list(), is(arrayWithSize(greaterThan(0)))); 
    }

    @Test public void
    createsAFileWithGivenNameAndExtension() throws Exception {
        runPrinter();
        File outputFile = new File(outputDir, "books.xml");
        assertThat(outputFile.exists(), is(true)); 
    }

    @Test public void
    serializesTheGivenXMLFile() throws Exception {
        runPrinter();
        Document outputDoc = builder.parse(new File(outputDir, "books.xml"));
        assertThat(outputDoc, hasXPath("/books/book", equalTo("The Big Sleep")));
    }

    private void runPrinter() {
        Printer printer = new Printer(outputDir);
        printer.receive("books", "xml", doc);
    }

    private Element addChildElement(Node node, String name) {
        Element element = doc.createElement(name);
        node.appendChild(element);
        return element;
    }

    private void addTextChild(Node node, String text) {
        Text textNode = doc.createTextNode(text);
        node.appendChild(textNode);
    }

    public static File makeTempDir(String name) throws Exception {
        File dir = File.createTempFile(name, Long.toString(System.nanoTime()));
        dir.delete();
        dir.mkdir();
        return dir;
    }
}

