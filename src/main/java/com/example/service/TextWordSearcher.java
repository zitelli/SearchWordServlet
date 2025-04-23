package com.example.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.example.model.Match;
import com.example.repositories.FileWordSeacher;

public class TextWordSearcher implements FileWordSeacher {

	public List<String> search(Path filePath, String word) throws IOException {
        return searchTextFile(filePath, word, false);
    }
	
    public static List<String> searchTextFile(Path filePath, String searchWord, boolean caseSensitive) throws IOException {
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        List<Match> matches = new ArrayList<>();
        List<String> status = new LinkedList<>();

        for (int i = 0; i < lines.size(); i++) {
            String originalLine = lines.get(i);
            String searchLine = caseSensitive ? originalLine : originalLine.toLowerCase();
            String searchTerm = caseSensitive ? searchWord : searchWord.toLowerCase();

            if (searchLine.contains(searchTerm)) {
                matches.add(new Match(i + 1, originalLine));
            }
        }

        if (matches.size() > 0) {
        	status.add("Occurences: " + matches.size());
        }	
//        matches.forEach(match ->
//            System.out.printf(
//            "Line %d: %s%n", 
//            match.lineNumber(), 
//            highlightMatch(match.lineContent(), searchWord, caseSensitive))
//        );
        for (Match match : matches) {
        	status.add(
                " - Line " + match.lineNumber() + ": " +
        	     highlightMatch(match.lineContent(), searchWord, caseSensitive)
            );
        }
        
        return status;
    }

    private static String highlightMatch(String line, String word, boolean caseSensitive) {
        String target = caseSensitive ? word : word.toLowerCase();
        String modifiedLine = caseSensitive ? line : line.toLowerCase();
        return modifiedLine.replaceAll("(?i)(" + Pattern.quote(target) + ")", ">>>$1<<<");
    }

//    public static int countMatchesLargeFile(String filePath, String searchWord) throws IOException {
//        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
//            return (int) lines
//                .filter(line -> line.contains(searchWord))
//                .count();
//        }
//    }

}