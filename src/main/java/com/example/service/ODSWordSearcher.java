package com.example.service;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import com.example.repositories.FileWordSeacher;

public class ODSWordSearcher implements FileWordSeacher {
	
	public List<String> search(Path filePath, String word) throws Exception {
        return searchODS(filePath, word);
    }
	
	private static List<String> searchODS(Path filePath, String searchWord) throws Exception {
        List<String> status = new LinkedList<>();
		OdfSpreadsheetDocument document = OdfSpreadsheetDocument.loadDocument(filePath.toFile());
        try {
            // Iterate through all tables (sheets)
            @SuppressWarnings("deprecation")
			Iterator<OdfTable> tableIterator = document.getTableList().iterator();
            while (tableIterator.hasNext()) {
                OdfTable table = tableIterator.next();
//                System.out.println("Searching sheet: " + table.getTableName());
                
                // Iterate through rows
                for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                    OdfTableRow row = table.getRowByIndex(rowIndex);
                    
                    // Iterate through cells
                    for (int cellIndex = 0; cellIndex < row.getCellCount(); cellIndex++) {
                        try {
                            String cellValue = row.getCellByIndex(cellIndex).getDisplayText().toLowerCase();
                            if (cellValue.contains(searchWord.toLowerCase())) {
                                status.add("Found '" + searchWord + "' in Sheet '" + table.getTableName() +
                                		   "' at Row "+ (rowIndex + 1) + ", Column " + (cellIndex + 1));
                                status.add("Cell content: " + cellValue);
                            }
                        } catch (Exception e) {
                            // Skip problematic cells
                            continue;
                        }
                    }
                }
            }
            
        } finally {
            document.close();
        }
        return status;
	}
    
//    public static void main(String[] args) {
//        try {
//            searchODS("/home/francisco/Documentos/workspace_pessoal_2/arquivos/ItapevaEstimativaPreco.ods", "Itapeva");
//        } catch (Exception e) {
//            System.err.println("Error processing ODS file: " + e.getMessage());
//        }
//    }
}