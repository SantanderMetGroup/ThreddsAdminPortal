package es.unican.meteo.tap.util;


import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Returns the result, a message and the created/updated 
 */
public class JtableJsonResponse<T> extends JtableJson<T>{
	
	@JsonProperty("Message")
	private String message;
	
	@JsonProperty("Record")
	private T record;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getRecord() {
		return record;
	}

	public void setRecord(T record) {
		this.record = record;
	}
}
