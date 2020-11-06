package com.coap.server.main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONObject;

public class Server extends CoapServer {
	
	final static String HTTP_URL = "http://localhost:8080";

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	}

	public Server() throws SocketException {
		add(new VoteResource());
		add(new PollResource());
	}
	
	class PollResource extends CoapResource {
		public PollResource() {
			super("poll");
			getAttributes().setTitle("Poll Resource");
		}

		public void handlePOST(CoapExchange exchange) {
			String pollId = new String(exchange.getRequestPayload());
			String response = "No Data";
			try {
				URL url = new URL(HTTP_URL + "/polls/result/" + pollId);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				response = ResponseBuilder.getReturnBody(con);
				System.out.println(con.getResponseCode() + con.getResponseMessage());
				con.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				exchange.respond("POST_REQUEST_FAILED");
			}
			exchange.respond(response);
		}

	}

	class VoteResource extends CoapResource {
		public VoteResource() {
			super("vote");
			getAttributes().setTitle("Vote Resource");
		}

		public void handlePOST(CoapExchange exchange) {
			JSONObject json = new JSONObject(new String(exchange.getRequestPayload()));
			try {
				
				URL url = new URL(HTTP_URL + "/votes/many");
				
				Map<String, String> parameters = new HashMap<>();
				parameters.put("poll", String.valueOf(json.getInt("poll")));
				parameters.put("red", String.valueOf(json.getInt("red")));
				parameters.put("green", String.valueOf(json.getInt("green")));
				byte[] postDataBytes = ParameterStringBuilder.getParamsString(parameters).getBytes("UTF-8");
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
				con.setDoOutput(true);
				
				DataOutputStream out = new DataOutputStream(con.getOutputStream());
				out.write(postDataBytes);
				out.flush();
				out.close();
				System.out.println("Vote resource [post] - Response code: " + con.getResponseCode());
				con.disconnect();
				
			} catch (IOException e) {
				e.printStackTrace();
				exchange.respond("POST_REQUEST_FAILED");
			} catch (Exception e) {
				e.printStackTrace();
				exchange.respond("POST_REQUEST_FAILED");
			}
			exchange.respond("POST_REQUEST_SUCCESS");
		}

	}

	static class ParameterStringBuilder {
		public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				result.append("&");
			}

			String resultString = result.toString();
			return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
		}
	}

	static class ResponseBuilder {
		public static String getFullResponse(HttpURLConnection con) throws IOException {
			StringBuilder fullResponseBuilder = new StringBuilder();

			// read status and message
			fullResponseBuilder.append(con.getResponseCode()).append(" ").append(con.getResponseMessage()).append("\n");

			// read headers
			con.getHeaderFields().entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> {
				fullResponseBuilder.append(entry.getKey()).append(": ");
				List<String> headerValues = entry.getValue();
				Iterator<String> it = headerValues.iterator();
				if (it.hasNext()) {
					fullResponseBuilder.append(it.next());
					while (it.hasNext()) {
						fullResponseBuilder.append(", ").append(it.next());
					}
				}
				fullResponseBuilder.append("\n");
			});

			// read response content
			String body = getReturnBody(con);

			return fullResponseBuilder.toString() + body;
		}
		
		public static String getReturnBody(HttpURLConnection con) throws IOException {
			BufferedReader in = null;
			if (con.getResponseCode() > 299) {
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			return content.toString();
		}
	}
}