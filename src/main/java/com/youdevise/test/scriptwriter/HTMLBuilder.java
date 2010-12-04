package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class HTMLBuilder implements TokenListener {
    private Recorder recorder;
    private String className;
    private Document doc;
    private Element root;

    public HTMLBuilder(Recorder recorder) { 
        this.recorder = recorder;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        root = doc.createElement("html");
        doc.appendChild(root);
    }

    @Override public void className(String className) { 
        this.className = className; 
        Element head = doc.createElement("head");
        root.appendChild(head);
        Element title = doc.createElement("title");
        head.appendChild(title);
        Text titleText = doc.createTextNode(className);
        title.appendChild(titleText);
    }

    public void output() {
        recorder.write(className, "html", doc);
    }
}
