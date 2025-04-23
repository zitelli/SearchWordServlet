package com.example.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.control.SearcherFactory;
import com.example.model.Arquivo;
import com.example.repositories.FileWordSeacher;

@WebServlet("/files")
public class SearchWordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String directory;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	// 1. Capture the directory parameter
    	directory = request.getParameter("directory");
        List<String> files = new ArrayList<>();

        // 2. Validate and list files in the directory
        if (directory != null && !directory.isEmpty()) {
            File dir = new File(directory);
            if (dir.exists() && dir.isDirectory()) {
                File[] listOfFiles = dir.listFiles();
                if (listOfFiles != null) {
                    for (File file : listOfFiles) {
                        if (file.isFile()) files.add(file.getName());
                    }
                }
            } else {
                request.setAttribute("error", "Invalid directory!");
            }
        }

        request.setAttribute("files", files);
        request.getRequestDispatcher("/WEB-INF/jsp/files.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String word = request.getParameter("word");    	
    	request.setAttribute("word", word);
    	
        String[] selectedFiles = request.getParameterValues("selectedFiles");
        ArrayList<String> fileList = new ArrayList<>(Arrays.asList(selectedFiles));
        // Change arquivos to a thread-safe collection if parallel processing
        ConcurrentLinkedQueue<Arquivo> arquivos = new ConcurrentLinkedQueue<>();

		List<String> globalStatus = Collections.synchronizedList(new LinkedList<>());
        
	    fileList.parallelStream().forEach(filename -> {
	        try {	
	            Path filePath = Paths.get(directory, filename);
	            String extension = getFileExtension(filename);

	            FileWordSeacher searcher = SearcherFactory.getSearcher(extension);
	            List<String> status = searcher.search(filePath, word);	    	        
    	        
	            synchronized(globalStatus) {
	                globalStatus.addAll(status);
	            }
	            
    	        if (!status.isEmpty()) {
    	            arquivos.add(new Arquivo(filename, status));
    	        }
	        } catch (Exception e) {
	            System.err.println("Error processing " + filename + ": " + e.getMessage());
	        }
	    });
	    
        if (arquivos.isEmpty()) {
        	globalStatus.add("Not found");
            arquivos.add(new Arquivo("Of those Chosen", globalStatus));
        }

        // You can now use the ArrayList
    	request.setAttribute("files", arquivos);
        request.getRequestDispatcher("/WEB-INF/jsp/selected.jsp").forward(request, response);
    }

    // Helper method for extension extraction
    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex > 0) ? filename.substring(dotIndex + 1).toLowerCase() : "";
    }
}
