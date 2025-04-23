package com.example.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import com.example.repositories.FileWordSeacher;

public class PPTXWordFinder implements FileWordSeacher {

	public List<String> search(Path filePath, String word) throws IOException {
        return searchTextFromAll(filePath, word);
    }
	
	private static List<String> searchTextFromAll(Path filePath, String targetWord) {
	    List<String> status = new LinkedList<>();
		try (FileInputStream fis = new FileInputStream(filePath.toString());
             XMLSlideShow pptx = new XMLSlideShow(fis)) {

            // Iterate through slides
            for (XSLFSlide slide : pptx.getSlides()) {
                String slideText = extractTextFromSlide(slide);
                if (slideText.contains(targetWord)) {
                    status.add(" - Found in slide #" + slide.getSlideNumber());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		return status;
	}

    // Extract text from a slide (including tables and groups)
    private static String extractTextFromSlide(XSLFSlide slide) {
        StringBuilder text = new StringBuilder();
        for (XSLFShape shape : slide.getShapes()) {
            extractTextFromShape(shape, text);
        }
        return text.toString();
    }

    // Recursively extract text from shapes, tables, and groups
    private static void extractTextFromShape(XSLFShape shape, StringBuilder text) {
        if (shape instanceof XSLFTextShape) {
            text.append(((XSLFTextShape) shape).getText()).append(" ");
        } else if (shape instanceof XSLFTable) {
            XSLFTable table = (XSLFTable) shape;
            for (XSLFTableRow row : table.getRows()) {
                for (XSLFTableCell cell : row.getCells()) {
                    extractTextFromShape(cell, text);
                }
            }
        } else if (shape instanceof XSLFGroupShape) {
            XSLFGroupShape group = (XSLFGroupShape) shape;
            for (XSLFShape child : group.getShapes()) {
                extractTextFromShape(child, text);
            }
        }
    }
}