package no.hvl.dat250.feedapp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseInitializer {
	
	@PostConstruct
	public void initialize() throws IOException, FirebaseAuthException{
		FileInputStream serviceAccount = new FileInputStream("./feedapp-56dfe-firebase-adminsdk-isgww-df9eb5fba2.json");
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setDatabaseUrl("https://feedapp-56dfe.firebaseio.com").build();
		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
	}
	
	public Firestore getDatabase() throws IOException {
		return FirestoreClient.getFirestore();
	}
}
