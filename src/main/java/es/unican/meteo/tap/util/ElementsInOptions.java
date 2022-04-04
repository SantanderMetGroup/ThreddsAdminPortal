package es.unican.meteo.tap.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ElementsInOptions<T> {
	@JsonProperty("Result")
	private String result = "OK";
	
	@JsonProperty("Options")
	private List<T> options;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<T> getOptions() {
		return options;
	}
	public void setOptions(List<T> options) {
		this.options = options;
	}
	
	
}
