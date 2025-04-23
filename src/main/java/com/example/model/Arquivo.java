package com.example.model;

import java.util.List;

public class Arquivo {

	private String name;
	private List<String> status;
	
	public Arquivo(String name, List<String> status) {
		this.name = name;
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	
}
