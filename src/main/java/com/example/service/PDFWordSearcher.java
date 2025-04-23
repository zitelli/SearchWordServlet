package com.example.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.example.repositories.FileWordSeacher;

public class PDFWordSearcher implements FileWordSeacher {

	public List<String> search(Path filePath, String word) throws IOException {
        return searchWordWithPages(filePath, word);
    }
	
//	private static void searchWordInPDF(String filePath, String searchWord) throws IOException {
//	    try (PDDocument document = PDDocument.load(new File(filePath))) {
//	        PDFTextStripper stripper = new PDFTextStripper();
//	        String text = stripper.getText(document);
//	        
//	        if (text.contains(searchWord)) {
//	            // Optional: Find all occurrences with positions
//	            int index = text.indexOf(searchWord);
//	            while (index >= 0) {
//	                index = text.indexOf(searchWord, index + 1);
//	            }
//	        }
//	    }
//	}
	
   private static List<String> searchWordWithPages(Path filePath, String searchWord) throws IOException {

       	try (PDDocument document = PDDocument.load(filePath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            
           	List<String> status = new LinkedList<>();
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                String pageText = stripper.getText(document);
                if (pageText.toLowerCase().contains(searchWord.toLowerCase())) {
                    int count = countOccurrences(pageText, searchWord);
                    if (count>0) {
                    	status.add("Page: " + page + " - occurrences: " + count);
                    }	
		        }
            }

            return status;
        }
    }
	
    private static int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

}	
