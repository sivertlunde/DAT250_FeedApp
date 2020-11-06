package com.coap.client;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.JSONObject;

public class DisplayDevice {

	final static String COAP_URL = "coap://localhost:5683";

	public static void main(String[] args) {

		CoapClient client = new CoapClient(COAP_URL + "/poll");

		System.out.println("SYNCHRONOUS");

		// synchronous
		try {
			while (true) {
				String content1 = client.post("40", MediaTypeRegistry.TEXT_PLAIN).getResponseText();
				JSONObject json = new JSONObject(content1);
				JSONObject redJson = (JSONObject) json.get("red");
				JSONObject greenJson = (JSONObject) json.get("green");
				System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				System.out.println("\nTitle:\t\t\t\t" + json.get("title"));
				System.out.println("Description:\t\t\t" + json.get("description"));
				System.out.println(redJson.get("text") + " (red votes):\t" + redJson.get("amount"));
				System.out.println(greenJson.get("text") + " (green votes):\t\t" + greenJson.get("amount"));
				Thread.sleep(10000);
			}

//			JSONObject json = new JSONObject();
//			json.put("green", 49);
//			json.put("red", 51);
//			json.put("poll", 25);
//			CoapResponse resp2 = client.post(json.toString(), MediaTypeRegistry.TEXT_PLAIN);
//			System.out.println("RESPONSE 2 CODE: " + resp2.getResponseText());
		} catch (ConnectorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

//		// asynchronous
//		
//		System.out.println("ASYNCHRONOUS (press enter to continue)");
//		
//		client.get(new CoapHandler() {
//			@Override public void onLoad(CoapResponse response) {
//				String content = response.getResponseText();
//				System.out.println("RESPONSE 3: " + content);
//			}
//			
//			@Override public void onError() {
//				System.err.println("FAILED");
//			}
//		});
//		
//		// wait for user
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		try { br.readLine(); } catch (IOException e) { }
//		
//		// observe
//
//		System.out.println("OBSERVE (press enter to exit)");
//		
//		CoapObserveRelation relation = client.observe(
//				new CoapHandler() {
//					@Override public void onLoad(CoapResponse response) {
//						String content = response.getResponseText();
//						System.out.println("NOTIFICATION: " + content);
//					}
//					
//					@Override public void onError() {
//						System.err.println("OBSERVING FAILED (press enter to exit)");
//					}
//				});
//		
//		// wait for user
//		try { br.readLine(); } catch (IOException e) { }
//		
//		System.out.println("CANCELLATION");
//		
//		relation.proactiveCancel();
	}
}
