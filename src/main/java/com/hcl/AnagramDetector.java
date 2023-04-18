package com.hcl;

import com.hcl.exceptions.AnagramExecutionException;
import com.hcl.exceptions.AnagramInitializationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AnagramDetector {
    public static final String ERROR_INVALID_PATH = "Error: Invalid path. You need to specify path of the desired input file.";
    public static final String ERROR_INVALID_PATH_FILE_DOES_NOT_EXIST = "Error: Invalid path. File does not exist on specified path.";
    public static final String ERROR_INVALID_FILE_TYPE = "Error: Invalid file type.";
    public static final String ERROR_INVALID_ARGUMENT = "Error: Invalid parameter please use -h flag to display help page";
    public static final String ERROR_EMPTY_PHRASE = "Error: Empty phrase";
    public static final String INPUT_HELP = "Enter the word/input/String";
    public static final String ERROR_READING_INPUT_FILE = "Error: Error reading input file";
    private final List<String> data = new ArrayList<>();

    private AnagramDetector(Path file) throws AnagramExecutionException {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new AnagramExecutionException(ERROR_READING_INPUT_FILE);
        }
    }

    public static AnagramDetector initWithArguments(String... args) throws AnagramInitializationException, URISyntaxException, AnagramExecutionException {
        if (args.length > 0) {
            String firstArgument = args[0];
            if (firstArgument.equals("-f")) {
                return initWithDifferentFile(args);
            }
            throw new AnagramInitializationException(ERROR_INVALID_ARGUMENT);
        }
        URL resource = AnagramDetector.class.getClassLoader().getResource("words.txt");
        assert resource != null;
        return new AnagramDetector(Paths.get(resource.toURI()));
    }

    private static AnagramDetector initWithDifferentFile(String[] args) throws AnagramInitializationException, AnagramExecutionException {
        if (args.length == 1) {
            throw new AnagramInitializationException(ERROR_INVALID_PATH);
        }
        String filePath = args[1];
        Path path = Paths.get(filePath);
        if (!path.toFile().exists()) {
            throw new AnagramInitializationException(ERROR_INVALID_PATH_FILE_DOES_NOT_EXIST);
        }
        if (!path.getFileName().toString().endsWith(".txt")) {
            throw new AnagramInitializationException(ERROR_INVALID_FILE_TYPE);
        }
        return new AnagramDetector(path);

    }

    public String readInput() {
        System.out.println(INPUT_HELP);
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            return sc.nextLine();
        } else {
            return "";
        }
    }

    public List<String> analyse(String content) throws AnagramExecutionException {
        if (content.isBlank()) {
            throw new AnagramExecutionException(ERROR_EMPTY_PHRASE);
        }
        return this.data.stream().filter(current -> isAnagram(content, current))
                .sorted()
                .collect(Collectors.toList());
    }

    public static boolean isAnagram(String content, String current) {
        if(content == null | current == null) return false;
        String contentWithoutSpaces = content.replaceAll(" ", "");
        String currentWithoutSpaces = current.replaceAll(" ", "");
        if(contentWithoutSpaces.isEmpty() | currentWithoutSpaces.isEmpty()) return false;
        char[] a1 = contentWithoutSpaces.toCharArray();
        char[] a2 = currentWithoutSpaces.toCharArray();
        Arrays.sort(a1);
        Arrays.sort(a2);
        return Arrays.equals(a1, a2);
    }
}
