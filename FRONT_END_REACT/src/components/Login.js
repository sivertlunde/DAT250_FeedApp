import React from 'react';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import firebase from 'firebase';

// Configure Firebase.
const config = {
    apiKey: "AIzaSyC0_DiIJMudH820HnJdOXu-MYFJdCFzVW0",
    authDomain: "feedapp-56dfe.firebaseapp.com",
    databaseURL: "https://feedapp-56dfe.firebaseio.com",
    projectId: "feedapp-56dfe",
    storageBucket: "feedapp-56dfe.appspot.com",
    messagingSenderId: "382153420094",
    appId: "1:382153420094:web:03e9463e68483eff64506f"
};
firebase.initializeApp(config);

// Configure FirebaseUI.
const uiConfig = {
  // Popup signin flow rather than redirect flow.
  signInFlow: 'popup',
  // Redirect to /signedIn after sign in is successful. Alternatively you can provide a callbacks.signInSuccess function.
  signInSuccessUrl: '/signedIn',
  // We will display Google and Facebook as auth providers.
  signInOptions: [
    firebase.auth.EmailAuthProvider.PROVIDER_ID
  ]
};

class Login extends React.Component {
  render() {
    return (
      <div>
        <h1>My App</h1>
        <p>Please sign-in:</p>
        <StyledFirebaseAuth uiConfig={uiConfig} firebaseAuth={firebase.auth()}/>
      </div>
    );
  }
}

export default Login
