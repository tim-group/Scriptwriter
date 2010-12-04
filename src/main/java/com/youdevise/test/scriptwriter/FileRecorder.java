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
import org.w3c.dom.Node;

public class FileRecorder implements Recorder {
    private File outputDir;

    public FileRecorder(File outputDir) { this.outputDir = outputDir; }
        
    @Override public void write(String title, String type, Node doc) { 
        outputDir.mkdir();
        File outputFile = new File(outputDir, title + "." + type);

        try { 
            Source source = new DOMSource(doc); 
            Result result = new StreamResult(outputFile); 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.transform(source, result);
        } catch (TransformerException e) { 
            e.printStackTrace(); // FIXXXXXX
        } 
    }
}
