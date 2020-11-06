package com.mqtt.publish;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {

	public static void main(String[] args) {
		
		String messageString = "";
		List<Integer> oldlist = new ArrayList<>();
		
		while(true) {
			try {
	            URL url = new URL("http://localhost:8080/polls/recentlyfinished");
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            
	            con.setRequestMethod("GET");
	            con.connect();
	            if(con.getResponseCode() == 200) {
	            	List<Integer> newlist = (List<Integer>) con.getContent();
	            	
	            	for(int i =0; i<= newlist.size(); i++) {
	            		if(!oldlist.contains(newlist.get(i))){
	            			System.out.println("== START PUBLISHER ==");
	            			URL url2 = new URL("http://localhost:8080/polls/result/"+newlist.get(i));
	        	            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
	        	            messageString = FullResponseBuilder.getFullResponse(con2);
		        			MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		        			client.connect();
		        			MqttMessage message = new MqttMessage();
		        			
		        			message.setPayload(messageString.getBytes());
		        			client.publish("iot_data", message);
		        			System.out.println("\tMessage '"+ messageString +"' to 'iot_data'");
		        			client.disconnect();
		        			client.close();
		        			
		        			System.out.println("== END PUBLISHER ==");
		        			
	            		}else {
	            		System.out.println("Poll with id: " + newlist.get(i) + " has already been sent");
	            		}
		            	
		            }
	            	oldlist = newlist;
	            }else if(con.getResponseCode() == 204) {
	            	System.out.println("Nothing to update");
	            }else {
	            	System.out.println("Da frick?");
	            }
	            //List<Integer> newlist = con.getInputStream(); 
	            String s = FullResponseBuilder.getFullResponse(con);
	            System.out.println(s);
	            /*
	            
	            */
	            Thread.sleep(1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	            
	        }
			//Thread.sleep(60000);
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
	
	/*
	static class FullResponseBuilderInteger {
        public static List<Integer> getFullResponse(HttpURLConnection con) throws IOException {
            
            // read response content
            BufferedReader in = null;
            if (con.getResponseCode() > 299) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            
            return in;
        }
    }
    */
}

