package com.frc5431.thingworx.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.frc5431.thingworx.json.Json;
import com.frc5431.thingworx.json.JsonArray;
import com.frc5431.thingworx.json.JsonObject;

public class ThingWorx {

	private HttpURLConnection connection = null; 
	private final String
		baseUrl = "http://52.202.207.237/Thingworx",
		appKey = "e5ced0ce-637f-4b33-a4d3-cc3597af0b71",
		userName = "smerkousdavid@gmail.com",
		userPass = "holycrap";
	
	private String
		selected_thing = "RobotData";
	
	private final String[]
			header_h = {"Connection",
					"X-Requested-With",
					"Content-Type",
					"DNT",
					"Accept-Encoding",
					"appKey",
					"Accept"},
			header_b = {"keep-alive",
					"XMLHttpRequest",
					"application/json",
					"1",
					"gzip, deflate",
					appKey,
					"application/json, application/json-compressed, text/javascript, */*, q=0.01"};
	
	public ThingWorx() {
		
		
	}
	
	public String put_property(String JSON) throws Exception {
		final String props_path = baseUrl + "/Things/" + selected_thing + "/Properties/*";
		System.out.println("Sending " + JSON + " To: " + props_path);
		return request("PUT", props_path, JSON);
	}
	
	public JsonObject get_property() throws Exception {
		final String props_path = baseUrl + "/Things/" + selected_thing + "/Properties/";
		String returned = request("GET", props_path, "");
		System.out.println("Got response " + returned + " From " + props_path);
		JsonObject total = Json.parse(returned).asObject();
		JsonArray rows = total.get("rows").asArray();
		return rows.get(0).asObject();
	}
	
	
	private String request(String type, String targetURL, String urlParameters) {
		   
		  try {
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod(type);
		    final String userpass = userName + ":" + userPass; 
		    final String auth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
		    connection.setRequestProperty("Authorization", auth);
		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    for(int ind = 0; ind < header_h.length; ind++) {
		    	connection.setRequestProperty(header_h[ind], header_b[ind]);
		    }
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    if(type == "PUT") {
			    DataOutputStream wr = new DataOutputStream (
			        connection.getOutputStream());
			    wr.writeBytes(urlParameters);
			    wr.close();
		    }
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
		    String line;
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
		}

	
}
