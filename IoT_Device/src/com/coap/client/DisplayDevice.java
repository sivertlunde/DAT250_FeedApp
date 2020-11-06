package com.coap.client;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.JSONObject;

public class DisplayDevice {

	final static String COAP_URL = "coap://localhost:5683";

	public static void main(String[] args) {

		CoapClient client = new CoapClient(COAP_URL + "/poll");

		Scanner in = new Scanner(System.in);
		System.out.println("Poll id: ");
		int pollId = Integer.parseInt(in.nextLine());
		in.close();
		// synchronous
		try {
			while (true) {
				String content1 = client.post(String.valueOf(pollId), MediaTypeRegistry.TEXT_PLAIN).getResponseText();
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
		} catch (ConnectorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
