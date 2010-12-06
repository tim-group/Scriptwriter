package com.youdevise.test.scriptwriter;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class FilePrinterTest {
    private File outputDir;
    private Document doc;

    @Before public void
    setupDocument() throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element books = addChildElement(doc, "books");
        Element bigSleep = addChildElement(books, "book");
        addTextChild(bigSleep, "The Big Sleep");
    }

    @Before public void
    createOutputDirectory() throws Exception {
        outputDir = makeTempDir("FilePrinter-test-output");
    }
    
    @Test public void 
    createsAFileInOutputDir() throws Exception {
        Printer printer = new FilePrinter(outputDir);
        printer.print("books", "xml", doc);
        assertTrue(outputDir.list().length > 0); // Replace with assertThat
    }

    @Test public void
    createsAFileWithGivenNameAndExtension() throws Exception {
        Printer printer = new FilePrinter(outputDir);
        printer.print("books", "xml", doc);
        String fileName = outputDir.list()[0];
        assertEquals("books.xml", fileName);
    }

// REMOVE DUPLICATION
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

