package AnalyticsComponent.AnalyticsComponent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;




public class FirestoreHandling {
	
	public static void saveInFirestore(String string){
		GoogleCredentials credentials;
		try {
			InputStream serviceAccount = new FileInputStream("C:/Users/areda/Documents/DAT250/NewTestWorkspace/DAT250_FeedApp/MQTT/feedapp-56dfe-firebase-adminsdk-isgww-df9eb5fba2.json");
			credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = FirebaseOptions.builder()
				    .setCredentials(credentials)
				    .setProjectId("feedapp-56dfe")
				    .build();
				FirebaseApp.initializeApp(options);
				JSONObject json = new JSONObject(string);
				
				Firestore db = FirestoreClient.getFirestore();
				DocumentReference docRef = db.collection("polls").document();
				ApiFuture<WriteResult> result = docRef.set(json.toMap());
				System.out.println("Update time : " + result.get().getUpdateTime());
				
				
				
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
		
		
	
	
}
