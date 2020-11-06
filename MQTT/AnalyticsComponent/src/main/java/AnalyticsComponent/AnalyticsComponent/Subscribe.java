package AnalyticsComponent.AnalyticsComponent;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class Subscribe {
  public static void main(String[] args) {
	
	  System.out.println("== START SUBSCRIBER ==");
	  
	  try {
		  MqttClient client=new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		  client.setCallback( new CallBack() );
		  client.connect(); 
		  client.subscribe("iot_data");
	  }catch(Exception e) {
		  e.printStackTrace();
	  }  
//    Example.clientId = String.format(
//        "%-23.23s",
//        (System.getProperty("user.name") + "_" + System.getProperty("clientId",
//            "Subscribe."))).trim();
//    try {
//    MqttClient client = new MqttClient(Example.TCPAddress, Example.clientId);
//      CallBack callback = new CallBack(Example.clientId);
//      client.setCallback(callback);
//      MqttConnectOptions conOptions = new MqttConnectOptions();
//      conOptions.setCleanSession(Example.cleanSession);
//      client.connect(conOptions);
//      System.out.println("Subscribing to topic \"" + Example.topicString
//          + "\" for client instance \"" + client.getClientId()
//          + "\" using QoS " + Example.QoS + ". Clean session is "
//          + Example.cleanSession);
//     	client.subscribe(Example.topicString, Example.QoS);
//      System.out.println("Going to sleep for " + Example.sleepTimeout / 1000
//          + " seconds");
//      Thread.sleep(Example.sleepTimeout);
//      client.disconnect();
//      System.out.println("Finished");
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
  
  
  
  
  } 
}