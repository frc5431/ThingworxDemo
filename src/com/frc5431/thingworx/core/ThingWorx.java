package com.frc5431.thingworx.core;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

public class ThingWorx {

	private HttpsURLConnection connection = null;
	private static final String baseUrl = "https://gj6mbgz0.pp.vuforia.io:8443/Thingworx",
			userName = "first", userPass = "Rob0t1cs", selected_thing = "academyRobot";
	private static final boolean printStuff = false; 
	private static final String[] header_h = { "Connection", "X-Requested-With", "Content-Type", "DNT", "Accept-Encoding",
			"Accept" },
			header_b = { "keep-alive", "XMLHttpRequest", "application/json", "1", "gzip, deflate",
					"application/json, application/json-compressed, text/javascript, */*, q=0.01" };

	private String props_path = "";
	
	public ThingWorx() {
		setThing(); //Sets the properties thing path
	}

	/*private void print_https_cert(HttpsURLConnection con) {

		if (con != null) {

			try {

				System.out.println("Response Code : " + con.getResponseCode());
				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					System.out.println("Cert Type : " + cert.getType());
					System.out.println("Cert Hash Code : " + cert.hashCode());
					System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
					System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
					System.out.println("\n");
				}

			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}*/
	
	public void setThing() {
		props_path = baseUrl + "/Things/" + selected_thing + "/Properties";
	}

	public JSONObject put_property(String JSON) throws Exception {
		if(printStuff) System.out.println("Sending " + JSON + " To: " + props_path);
		return request("PUT", props_path, JSON);
	}

	public JSONObject get_property() throws Exception {
		try {
			//final String props_path = baseUrl + "/Things/" + selected_thing + "/Properties/";
			//System.out.println("GETTING STUFF: " + String.valueOf(props_path));
			JSONObject returned = request("GET", props_path, "");
			if(printStuff)
				System.out.println("Got response " + returned.toString() + " From " + props_path);
			//JSONObject total = new JSONObject(returned);
			JSONArray rows = returned.getJSONArray("rows");
			return (JSONObject) rows.get(0);

		} catch (Throwable t) {
			// ignored
		}
		return null;
	}

	private JSONObject request(String type, String targetURL, String urlParameters) {

		try {
			GetRequest request = Unirest.get(targetURL);
			for (int ind = 0; ind < header_h.length; ind++) {
				request.header(header_h[ind], header_b[ind]);
			}
			request.basicAuth(userName, userPass);
			return request.asJson().getBody().getObject();
		} catch (Exception e) {
			e.printStackTrace();
			if(printStuff) {
				System.out.println("Failed request!");
			}
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
