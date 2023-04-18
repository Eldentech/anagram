package com.hcl;

import com.hcl.exceptions.AnagramException;

import java.util.List;

public class Main {

    public static final String HELP = """
            Anagram application:
                            
            -f <file-input> Optional. File to use when detecting anagrams. In case you did not specify\s
            it will use internal data to test anagrams.\s
                            
            Once application initialized please enter phrase you want to find anagrams.
            """;
    public static final String ERROR_INTERNAL = "Error: Internal";

    public static void main(String... args) {
        if(args.length > 0 && args[0].equals("-h")) {
            System.out.println(HELP);
            System.exit(0);
        }
        try {
            AnagramDetector anagramDetector = AnagramDetector.initWithArguments(args);
            String input = anagramDetector.readInput();
            List<String> report  = anagramDetector.analyse(input);
            report.forEach(System.out::println);
            System.exit(0);
        } catch (Exception e) {
            if (e instanceof AnagramException) {
                System.out.println(e.getMessage());
            } else {
                System.out.println(ERROR_INTERNAL);
            }
            System.exit(1);
        }

    }
}
