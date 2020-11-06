package AnalyticsComponent.AnalyticsComponent;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import com.google.gson.Gson;

public class CallBack implements MqttCallback {
	
	public void connectionLost(Throwable throwable) {
	    System.out.println("Connection to MQTT broker lost!");
	  }

	  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
	    System.out.println("Message received:\n\t"+ new String(mqttMessage.getPayload()) );
	    Gson g = new Gson();
	    String str = g.toJson(new String(mqttMessage.getPayload())); 
	  }

	  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	    // not used in this example
	  }

	
	
	/*
	private String instanceData="";
	public CallBack(String instance) {
		instanceData = instance;
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) {
		
		try {
			System.out.println("Message arrived: \"" + message.toString()
	          + "\" on topic \"" + topic.toString() + "\" for instance \""
	          + instanceData + "\"");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	  
	public void connectionLost(Throwable cause) {
		    System.out.println("Connection lost on instance \"" + instanceData
		        + "\" with cause \"" + cause.getMessage() + "\" Reason code " 
		        + ((MqttException)cause).getReasonCode() + "\" Cause \"" 
		        + ((MqttException)cause).getCause() +  "\"");    
		    cause.printStackTrace();
		  }
	
	public void deliveryComplete(IMqttDeliveryToken token) {
		    try {
		      System.out.println("Delivery token \"" + token.hashCode()
		          + "\" received by instance \"" + instanceData + "\"");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
	*/

}
