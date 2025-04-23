package com.example.control;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.repositories.FileWordSeacher;
import com.example.service.ExcelWordSearcher;
import com.example.service.ImageProcessor;
import com.example.service.ODSWordSearcher;
import com.example.service.ODTWordSearcher;
import com.example.service.PDFWordSearcher;
import com.example.service.PPTXWordFinder;
import com.example.service.TextWordSearcher;

public class SearcherFactory {
	
    private static final Map<String, FileWordSeacher> searchers = new HashMap<>();
    
    static {
        searchers.put("pdf", new PDFWordSearcher());
        searchers.put("txt", new TextWordSearcher());
        searchers.put("xml", new TextWordSearcher());
        searchers.put("css", new TextWordSearcher());
        searchers.put("js", new TextWordSearcher());
        searchers.put("java", new TextWordSearcher());
        searchers.put("png", new ImageProcessor());
        searchers.put("jpg", new ImageProcessor());
        searchers.put("ods", new ODSWordSearcher());
        searchers.put("xlsx", new ExcelWordSearcher());
        searchers.put("xls", new ExcelWordSearcher());
        searchers.put("pptx", new PPTXWordFinder());
        searchers.put("odt", new ODTWordSearcher());
    }
    
    public static FileWordSeacher getSearcher(String extension) {
        return searchers.getOrDefault(extension, new UnsupportedFileSearcher());
    }
    
    private static class UnsupportedFileSearcher implements FileWordSeacher {
        @Override
        public List<String> search(Path filePath, String word) {
            return Collections.singletonList("Unsupported file type");
        }
    }
}
