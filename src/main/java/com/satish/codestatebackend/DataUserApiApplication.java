package com.satish.codestatebackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class DataUserApiApplication {
	
	final static String serverUrl = "http://localhost:9090";

	public static void main(String[] args) {
		SpringApplication.run(DataUserApiApplication.class, args);
	}
	
	public static String requestProcessData(String url) {
		RestTemplate request = new RestTemplate();
		String result = request.getForObject(serverUrl + "/"+url, String.class);
		System.out.println(result);
		return result;
	}
	
	@GetMapping("codeToState")
	public String getCodeToState(@RequestParam String code) {
		String state = null;
		String response = requestProcessData("readDataForCode");
		try {
			JSONObject json = new JSONObject(response);
		
			state = json.getString(code.toUpperCase());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(state==null) {
			return "NO MATCH FOUND for code: "+code;
		}
		
		return state;
	}
	
	@GetMapping("stateToCode")
	public String stateToCode(@RequestParam String state) {
		String value = null;
		String response = requestProcessData("readDataForState");
		try {
			JSONArray arr = new JSONArray(response);
			for(int i=0;i<arr.length();i++) {
				JSONObject json = arr.getJSONObject(i);
				if(json.getString("name").equalsIgnoreCase(state)) {
					value = json.getString("abbreviation");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(value==null) {
			return "NO MATCH FOUND for state: "+state;
		}
		
		return value;
	}

}
