package com.simbu;

import org.springframework.http.HttpHeaders;

public class RestUtils {

	HttpHeaders headers = new HttpHeaders();
	static String Url="http://localhost:8080/";
	public HttpHeaders getHeader(){
	headers.set("Content-Type", "application/json");
	return headers;
	}
}
