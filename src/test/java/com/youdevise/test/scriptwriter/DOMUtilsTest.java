package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;

public class DOMUtilsTest {
    private Document doc;

    @Before public void
    setupDocument() throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
    
    @Test public void 
    addsRootNode() throws Exception {
        DOMUtils.addChildElement(doc, "pets");
        assertThat(doc, hasXPath("/pets"));
    }

    @Test public void 
    returnsAddedElement() throws Exception {
        Element root = DOMUtils.addChildElement(doc, "pets");
        assertThat(root.getTagName(), equalTo("pets"));
    }

    @Test public void 
    addsChildNode() throws Exception {
        Element root = DOMUtils.addChildElement(doc, "pets");
        DOMUtils.addChildElement(root, "pet");
        assertThat(doc, hasXPath("/pets/pet"));
    }

    @Test public void 
    addsTextNode() throws Exception {
        Element root = DOMUtils.addChildElement(doc, "pets");
        Element pet = DOMUtils.addChildElement(root, "pet");
        DOMUtils.addTextChild(pet, "Rover");
        assertThat(doc, hasXPath("/pets/pet", equalTo("Rover")));
    }
}

