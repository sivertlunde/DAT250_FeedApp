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
			credentials = GoogleCredentials.getApplicationDefault();
			FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials)
				    .setProjectId("feedapp-56dfe")
				    .build();
				FirebaseApp.initializeApp(options);

				Firestore db = FirestoreClient.getFirestore();
				
				JSONObject json = new JSONObject(string);
				
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
		
		
	
	
}
