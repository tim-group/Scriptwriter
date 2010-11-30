package com.youdevise.test.scriptwriter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConfigurationTest {
    private static final String CODE_FILE = "Code.java";
    private static final String DEFAULT_OUTPUT_DIR = "output";

    @Test public void
    parsesSingleArgumentAsCodeFile() throws Exception {
        Configuration config = new Configuration();
        config.parse(new String[] { CODE_FILE });

        assertEquals(config.codeFile.getPath(), CODE_FILE);
    }

    @Test public void
    parsesDefaultOutputDirectoryUsedWhenNoneSpecified() throws Exception {
        Configuration config = new Configuration();
        config.parse(new String[] { DEFAULT_OUTPUT_DIR });

        assertEquals(config.outputDir.getPath(), DEFAULT_OUTPUT_DIR);
    }
}
