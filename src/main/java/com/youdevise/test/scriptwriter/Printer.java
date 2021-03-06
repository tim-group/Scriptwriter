package com.youdevise.test.scriptwriter;

import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class Printer implements DocumentReceiver {
    private File outputDir;

    public Printer(File outputDir) { this.outputDir = outputDir; }
        
    @Override public void receive(String title, String type, Document doc) { 
        outputDir.mkdir();
        File outputFile = new File(outputDir, title + "." + type);

        DocumentType doctype = doc.getDoctype();

        try { 
            Source source = new DOMSource(doc); 
            Result result = new StreamResult(outputFile); 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

            transformer.transform(source, result);
        } catch (TransformerException e) { 
            throw new PrintingException("Failed during transform", e);
        } 
    }
}
