package com.coap.client;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.JSONObject;

public class VotingDevice {

	final static String COAP_URL = "coap://localhost:5683";

	public static void main(String[] args) {

		CoapClient client = new CoapClient(COAP_URL + "/vote");
		client.ping();

		Scanner in = new Scanner(System.in);
		boolean stop = false;
		System.out.println("Poll id: ");
		int pollId = Integer.parseInt(in.nextLine());
		System.out.println("You have the following options:\n"
				+ "Press 1:\tAdd 1 red vote\n"
				+ "Press 2:\tAdd 1 green vote\n"
				+ "Press 3:\tReset your current vote count\n"
				+ "Press 4:\tSubmit your votes\n"
				+ "Press 5:\tView your unsubmitted votes\n\n"
				+ "Press 9:\tStop the voting device");
		// synchronous
		try {
			int red = 0;
			int green = 0;
			while (!stop) {
				int input = Integer.parseInt(in.nextLine());
				switch(input) {
				case 1:
					red++;
					break;
				case 2:
					green++;
					break;
				case 3:
					red = 0;
					green = 0;
					System.out.println("Clearing votes");
					break;
				case 4:
					System.out.println("Do you want to submit " + red + " red votes, and " + green + " green votes? (y/n)");
					if (String.valueOf(in.nextLine()).equalsIgnoreCase("y")) {
						System.out.println("Submitting votes...");
						JSONObject json = new JSONObject();
						json.put("green", green);
						json.put("red", red);
						json.put("poll", pollId);
						client.post(json.toString(), MediaTypeRegistry.TEXT_PLAIN);
						red = 0;
						green = 0;
					}
					break;
				case 5:
					System.out.println("Red votes: " + red + ", Green votes: " + green);
					break;
				case 9:
					stop = true;
					in.close();
					break;
				default:
					break;
				}
			}
		} catch (ConnectorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
