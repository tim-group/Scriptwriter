package com.youdevise.test.scriptwriter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

public class ConfigurationTest {
    private static final String CODE_FILE = "Code.java";
    private static final String DEFAULT_OUTPUT_DIR = "output";
    private static final String CUSTOM_OUTPUT_DIR = "/home/me/my_output";

    @Test public void
    parsesSingleArgumentAsCodeFile() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { CODE_FILE });
        assertEquals(config.codeFile.getPath(), CODE_FILE);
    }

    @Test public void
    usesDefaultOutputDirectoryUsedWhenNoneSpecified() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { CODE_FILE });
        assertEquals(config.outputDir.getPath(), DEFAULT_OUTPUT_DIR);
    }

    @Test public void
    parsesOutputOption() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { CODE_FILE, "-o", CUSTOM_OUTPUT_DIR });
        assertEquals(config.outputDir.getPath(), CUSTOM_OUTPUT_DIR);
    }

    @Test(expected=ConfigurationException.class) public void
    throwsExceptionWhenCalledWithZeroArguments() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { });
    }

    @Test(expected=ConfigurationException.class) public void
    throwsExceptionWhenCalledWithUnknownOption() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { CODE_FILE, "-x", CUSTOM_OUTPUT_DIR});
    }

    @Test(expected=ConfigurationException.class) public void
    throwsExceptionWhenCalledWithMissingOptionSwitch() throws Exception {
        Configuration config = makeConfigurationFor(new String[] { CODE_FILE, CUSTOM_OUTPUT_DIR});
    }

    @Test public void
    createsUsageMessage() throws Exception {
        Configuration config = new Configuration();
        assertThat(config.usage, allOf(containsString("The Java file to be converted - required"), 
                                       containsString("The output directory - optional, defaults to 'output'")));
    }

    private Configuration makeConfigurationFor(String [] args) throws Exception {
        Configuration config = new Configuration();
        config.parse(args);
        return config;
    }
}
