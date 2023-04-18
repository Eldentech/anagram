package com.hcl;


import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;


@EnabledForJreRange(max = JRE.JAVA_17)
class MainTest {

    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    public void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void program_whenCalledWithHelpFlag_shouldDisplayHelp() {
        Main.main("-h");
        assertEquals(Main.HELP, outputStreamCaptor.toString());
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void program_whenCalledWithFileFlagWithoutArgument_shouldDisplayError() {
        Main.main("-f");
        assertEquals(AnagramDetector.ERROR_INVALID_PATH, outputStreamCaptor.toString());
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void program_whenCalledWithFileFlagWithInvalidArgument_shouldDisplayError() {
        Main.main("-f", "/not-exist.txt");
        assertEquals(AnagramDetector.ERROR_INVALID_PATH_FILE_DOES_NOT_EXIST, outputStreamCaptor.toString());
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void program_whenCalledWithFileFlagWithInvalidFileType_shouldDisplayError() {
        URL url = this.getClass().getClassLoader().getResource("invalid-file.csv");
        assert url != null;
        Main.main("-f", url.getPath());
        assertEquals(AnagramDetector.ERROR_INVALID_FILE_TYPE, outputStreamCaptor.toString());
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void program_whenCalledWithInvalidArgument_shouldDisplayError() {
        Main.main("-a");
        assertEquals(AnagramDetector.ERROR_INVALID_ARGUMENT, outputStreamCaptor.toString());
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void program_whenCalledProperly_shouldWork() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("tac".getBytes());
        System.setIn(in);
        Main.main();
        System.setIn(sysInBackup);
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void program_whenCalledWithEmptyPhrase_shouldError() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        System.setIn(in);
        Main.main();
        System.setIn(sysInBackup);
    }

}
