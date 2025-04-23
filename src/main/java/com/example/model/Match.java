package com.example.model;

//Replace the record with this class
public class Match {
	
 private final int lineNumber;
 private final String lineContent;

 public Match(int lineNumber, String lineContent) {
     this.lineNumber = lineNumber;
     this.lineContent = lineContent;
 }

 // Getters
 public int lineNumber() { return lineNumber; }
 public String lineContent() { return lineContent; }
}
