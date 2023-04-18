package com.hcl;


import com.hcl.exceptions.AnagramExecutionException;
import com.hcl.exceptions.AnagramInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnagramDetectorTest {

    @Test
    public void anagramDetector_whenCalledWithFileFlagWithoutArgument_shouldThrowError() {
        AnagramInitializationException exception = assertThrows(AnagramInitializationException.class,
                () -> AnagramDetector.initWithArguments("-f"));
        assertEquals(AnagramDetector.ERROR_INVALID_PATH, exception.getMessage());
    }

    @Test
    public void anagramDetector_whenCalledWithFileFlagWithInvalidArgument_shouldThrowError() {
        AnagramInitializationException exception = assertThrows(AnagramInitializationException.class,
                () -> AnagramDetector.initWithArguments("-f", "/not-exist.txt"));
        assertEquals(AnagramDetector.ERROR_INVALID_PATH_FILE_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    public void anagramDetector_whenCalledWithFileFlagWithInvalidFileType_shouldThrowError() {
        URL url = this.getClass().getClassLoader().getResource("invalid-file.csv");
        assert url != null;
        AnagramInitializationException exception = assertThrows(AnagramInitializationException.class,
                () -> AnagramDetector.initWithArguments("-f", url.getPath()));
        assertEquals(AnagramDetector.ERROR_INVALID_FILE_TYPE, exception.getMessage());
    }

    @Test
    public void anagramDetector_whenCalledWithInvalidArgument_shouldThrowError() {
        AnagramInitializationException exception = assertThrows(AnagramInitializationException.class,
                () -> AnagramDetector.initWithArguments("-a"));
        assertEquals(AnagramDetector.ERROR_INVALID_ARGUMENT, exception.getMessage());
    }

    @Test
    public void anagramDetector_whenCalledNoArgument_shouldWorkCorrectly() throws AnagramInitializationException, URISyntaxException, AnagramExecutionException {
        AnagramDetector anagramDetector = AnagramDetector.initWithArguments();
        assertNotNull(anagramDetector);
        List<String> report = anagramDetector.analyse("tac");
        assertEquals(List.of("act", "cat"), report);
    }

    @Test
    public void anagramDetector_whenCalledWithValidFile_shouldWorkCorrectly() throws AnagramInitializationException, URISyntaxException, AnagramExecutionException {
        URL url = this.getClass().getClassLoader().getResource("valid-file.txt");
        assert url != null;
        AnagramDetector anagramDetector = AnagramDetector.initWithArguments("-f", url.getPath());
        assertNotNull(anagramDetector);
        List<String> report = anagramDetector.analyse("tac");
        assertEquals(List.of("act", "cat"), report);
    }

    @ParameterizedTest
    @CsvSource({
            ",,false",
            "team,animal,false",
            "meal,lead,false",
            "tac,cat,true",
            "rear,rare,true",
            "aab,baa,true",
            "a ab,ba a,true",
            "tab,bat,true",
    })
    public void anagramDetector_whenIsAnagramCalled_shouldWork(String first, String second, boolean result) {
        assertEquals(result, AnagramDetector.isAnagram(first, second));
    }

}
