package es.unican.meteo.tap.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResponse {

	@JsonProperty("Result")
	private String result;
	
	@JsonProperty("Message")
	private Object message;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
}