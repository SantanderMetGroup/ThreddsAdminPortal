package es.unican.meteo.tap.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JtableJsonOptionsResponse{
	
	@JsonProperty("Value")
	String id;
	
	@JsonProperty("DisplayText")
	String name;
 	
	public JtableJsonOptionsResponse(String id, String name) {
	    this.id = id;
	    this.name = name;
	}
 	
 	public JtableJsonOptionsResponse(){}
 	
	public String getId() {
	    return id;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}
	
	
}