package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileRecorder implements Recorder {
    private File outputDir;

    public FileRecorder(File outputDir) { this.outputDir = outputDir; }
        
    @Override public void write(String title, String type, String contents) {
        outputDir.mkdir();
        File outputFile = new File(outputDir, title + "." + type);

        try {
            FileUtils.writeStringToFile(outputFile, contents);
        } catch (IOException e) {
            throw new IllegalStateException("cannot write to file " + outputFile.getAbsolutePath());
        }
    }
}
