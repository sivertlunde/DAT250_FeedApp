package com.coap.client;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.JSONObject;

public class VotingDevice {

	final static String COAP_URL = "coap://localhost:5683";

	public static void main(String[] args) {

		CoapClient client = new CoapClient(COAP_URL + "/vote");

		System.out.println("SYNCHRONOUS");

		// synchronous
		try {
			String content1 = client.get().getResponseText();
			System.out.println("RESPONSE 1: " + content1);

			JSONObject json = new JSONObject();
			json.put("green", 3);
			json.put("red", 5);
			json.put("poll", 40);
			CoapResponse resp2 = client.post(json.toString(), MediaTypeRegistry.TEXT_PLAIN);
			System.out.println("RESPONSE 2 CODE: " + resp2.getResponseText());
		} catch (ConnectorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
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
