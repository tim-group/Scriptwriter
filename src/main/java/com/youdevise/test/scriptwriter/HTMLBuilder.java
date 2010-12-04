package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
        Element head = addChildElement(root, "head");
        Element title = addChildElement(head, "title");
        addTextChild(title, className);
    }

    public void output() {
        recorder.write(className, "html", doc);
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
}
