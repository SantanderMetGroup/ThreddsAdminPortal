package es.unican.meteo.tap.util;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ResponseMessage{
		
	String message;
	String response;
	
	
	public ResponseMessage(){
		super();
	}
	
	public ResponseMessage(String message, String response) {
		super();
		this.message = message;
		this.response = response;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}



	public class ResponseMessageElement<T> extends ResponseMessage{
		T data;
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
	}

	public class ResponseMessageList<T> extends ResponseMessage{
		List<T> data;
		public List<T> getData() {
			return data;
		}
		public void setData(List<T> data) {
			this.data = data;
		}
	}
	
}

