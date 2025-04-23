package com.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.repositories.FileWordSeacher;

public class ExcelWordSearcher implements FileWordSeacher {
	
	public List<String> search(Path filePath, String word) throws IOException {
        return searchInExcel(filePath, word, false, false);
    }
	
    private static List<String> searchInExcel(Path filePath, String searchWord, 
                       boolean caseSensitive, boolean exactMatch) throws IOException {
    	
        try ( Workbook workbook = getWorkbook(filePath.toString()) ) {

            int totalOccurrences = 0;
            String searchTerm = caseSensitive ? searchWord : searchWord.toLowerCase();
           	List<String> status = new LinkedList<>();
           	
            for (Sheet sheet : workbook) {
                int sheetOccurrences = 0;
                
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        String cellValue = getCellValueAsString(cell);
                        String compareValue = caseSensitive ? cellValue : cellValue.toLowerCase();
                        
                        if (exactMatch) {
                            // Split cell content into words for exact matching
                            String[] words = compareValue.split("\\s+");
                            for (String word : words) {
                                if (word.equals(searchTerm)) {
                                    printMatch(status, sheet, row, cell, cellValue);
                                    sheetOccurrences++;
                                    totalOccurrences++;
                                }
                            }
                        } else {
                            if (compareValue.contains(searchTerm)) {
                                printMatch(status, sheet, row, cell, cellValue);
                                sheetOccurrences++;
                                totalOccurrences++;
                            }
                        }
                    }
                }
                
                if (sheetOccurrences > 0) {
                	status.add("Sheet: " + sheet.getSheetName() + " | occurrences: " + sheetOccurrences);
                    status.add(" - Total occurrences in workbook: " + totalOccurrences);
                }
            }
            
            return status;
        }
    }

    private static void printMatch(List<String> status, Sheet sheet, Row row, Cell cell, String cellValue) {
    	status.add("Cell: " + cell.getAddress().formatAsString());
//        System.out.println("Found match in:");
//        System.out.println("Sheet: " + sheet.getSheetName());
//        System.out.println("Row: " + (row.getRowNum() + 1));
//        System.out.println("Column: " + (cell.getColumnIndex() + 1));
//        System.out.println("Cell address: " + cell.getAddress().formatAsString());
//        System.out.println("Cell value: " + cellValue);
//        System.out.println("----------------------");
    }

    private static String getCellValueAsString(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    private static Workbook getWorkbook(String filePath) throws IOException {
    	FileInputStream fis = new FileInputStream(new File(filePath));
        if (filePath.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(fis); // For older .xls format
        }
        throw new IllegalArgumentException("The specified file is not an Excel file");
    }

}