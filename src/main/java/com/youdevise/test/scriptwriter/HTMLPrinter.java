package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class HTMLPrinter implements TokenListener {
    private Recorder recorder;
    private String className;
    private Document doc;

    public HTMLPrinter(Recorder recorder) { 
        this.recorder = recorder;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace(); // FIXXXXXX
        }
    }

    @Override public void start() { 
        Element root = doc.createElement("html");
        doc.appendChild(root);
    }
    @Override public void giveClassName(String className) { 
        this.className = className; 
        Element head = doc.createElement("head");
        doc.getFirstChild().appendChild(head);
        Element title = doc.createElement("title");
        head.appendChild(title);
        Text titleText = doc.createTextNode(className);
        title.appendChild(titleText);
    }
    @Override public void finish() { }

    public void print() {
        recorder.write(className, "html", doc);
    }
}
