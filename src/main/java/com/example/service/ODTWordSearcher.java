package com.example.service;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfDocumentNamespace;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.w3c.dom.NodeList;

import com.example.repositories.FileWordSeacher;

public class ODTWordSearcher implements FileWordSeacher {
	
	@Override
	public List<String> search(Path filePath, String word) throws Exception {
        return searchODT(filePath, word);
    }
	
    private static List<String> searchODT(Path filePath, String searchWord) throws Exception {
        List<String> status = new LinkedList<>();
        OdfTextDocument document = OdfTextDocument.loadDocument(filePath.toFile());
        try {
            // Get all paragraph elements
            NodeList paragraphs = document.getContentDom()
                .getElementsByTagNameNS(OdfDocumentNamespace.TEXT.getUri(), "p");
            
            for (int i = 0; i < paragraphs.getLength(); i++) {
                TextPElement paragraph = (TextPElement) paragraphs.item(i);
                String text = paragraph.getTextContent();
                
                if (text.toLowerCase().contains(searchWord.toLowerCase())) {
                    status.add("Found in paragraph " + (i + 1) + ": " + highlightMatch(text, searchWord));
                }
            }

        } finally {
            document.close();
        }
        return status;
    }

    private static String highlightMatch(String text, String word) {
        return text.replaceAll("(?i)(" + word + ")", ">>>$1<<<");
    }

}