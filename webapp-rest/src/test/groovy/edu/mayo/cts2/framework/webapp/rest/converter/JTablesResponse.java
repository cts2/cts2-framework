package edu.mayo.cts2.framework.webapp.rest.converter;

import java.util.ArrayList;
import java.util.List;

public class JTablesResponse {
	
	private String Result = "OK";
	
	private List<Object> Records = new ArrayList<Object>();
	
	public JTablesResponse() {
		super();
	}

	public JTablesResponse(List<Object> records) {
		super();
		Records = records;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public List<?> getRecords() {
		return Records;
	}

	public void setRecords(List<Object> records) {
		Records = records;
	}
}