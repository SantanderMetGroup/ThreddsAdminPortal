package es.unican.meteo.tap.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Returns the basic response in Jtable 
 */
public class JtableJson<T> {
	
	@JsonProperty("Result")
	private String result = "OK";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
