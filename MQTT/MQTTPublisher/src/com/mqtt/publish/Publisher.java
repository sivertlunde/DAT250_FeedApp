package com.mqtt.publish;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {

	public static void main(String[] args) {
		
		String messageString = "Hello World from Java!";

	    if (args.length == 2 ) {
	      messageString = args[1];
	    }
		
	    try {
            URL url = new URL("http://localhost:8080/polls/finishedBy");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            messageString = FullResponseBuilder.getFullResponse(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
	    
	    
	    
	    System.out.println("== START PUBLISHER ==");
	    
		try {
			MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			client.connect();
			MqttMessage message = new MqttMessage();
			
			message.setPayload(messageString.getBytes());
			client.publish("iot_data", message);
			System.out.println("\tMessage '"+ messageString +"' to 'iot_data'");
			client.disconnect();
			client.close();
			
			System.out.println("== END PUBLISHER ==");
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	static class FullResponseBuilder {
        public static String getFullResponse(HttpURLConnection con) throws IOException {
            
            // read response content
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

            return  content.toString();
        }
    }
}

