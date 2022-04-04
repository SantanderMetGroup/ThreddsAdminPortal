package es.unican.meteo.tap.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Returns list of records as required in Jtable 
 */

public class JtableJsonListResponse<T> extends JtableJson<T> {

	
	@JsonProperty("TotalRecordCount")
	private int totalRecordCount;
	
	@JsonProperty("Message")
	private String message;
	
	@JsonProperty("Records")
	private List<T> records;
	
	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}
}
