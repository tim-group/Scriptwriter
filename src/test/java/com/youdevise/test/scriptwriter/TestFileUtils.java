package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.IOException;

public class TestFileUtils {
    public static File makeTempDir(String name) throws IOException {
        File dir = File.createTempFile(name, Long.toString(System.nanoTime()));
        dir.delete();
        dir.mkdir();
        return dir;
    }
}
