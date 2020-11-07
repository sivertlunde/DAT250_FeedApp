import React from 'react';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import firebase from 'firebase';
import Cookies from 'universal-cookie';


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
    // Redirect to / after sign in is successful. Alternatively you can provide a callbacks.signInSuccess function.
    signInSuccessUrl: '/',

    // callbacks: {
    //     // Avoid redirects after sign-in.
    //     signInSuccessWithAuthResult: () => false
    //   },

    // We will display email as auth provider.
    signInOptions: [
        firebase.auth.EmailAuthProvider.PROVIDER_ID
    ]
};

const cookies = new Cookies();

class Login extends React.Component {
    componentDidMount() {
        this.unregisterAuthObserver = firebase.auth().onAuthStateChanged(
            (user) => {
                cookies.set("user", user);
            }
        );
        console.log(firebase.auth().currentUser);
    }

    componentWillUnmount() {
        this.unregisterAuthObserver();
    }

    render() {
        if (firebase.auth().currentUser != null) {
            return (
                <div>
                  <h1>FeedApp</h1>
                  <p>Signed in as {cookies.get("user").displayName}!</p>
                  <button onClick={() => {firebase.auth().signOut(); window.location.reload()}}>Sign-out</button>
                </div>
              );
        }
        
        return (
            <div>
                <h1>Sign in to FeedApp</h1>
                <p>Please sign-in:</p>
                <StyledFirebaseAuth uiConfig={uiConfig} firebaseAuth={firebase.auth()} />
            </div>
        );
    }
}

export default Login
