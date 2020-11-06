package AnalyticsComponent.AnalyticsComponent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;




public class FirestoreHandling {
	
	public void saveInFirestore(String string){
		GoogleCredentials credentials;
		try {
			credentials = GoogleCredentials.getApplicationDefault();
			FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials)
				    .setProjectId("feedapp-56dfe")
				    .build();
				FirebaseApp.initializeApp(options);

				Firestore db = FirestoreClient.getFirestore();
				
				DocumentReference docRef = db.collection("users").document("alovelace");
				// Add document data  with id "alovelace" using a hashmap
				Map<String, Object> data = new HashMap<>();
				data.put("first", "Ada");
				data.put("last", "Lovelace");
				data.put("born", 1815);
				//asynchronously write data
				ApiFuture<WriteResult> result = docRef.set(data);
				// ...
				// result.get() blocks on response
				System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
		
		
	
	
}
