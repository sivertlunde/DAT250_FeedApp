import logo from './logo.svg';
import './App.css';
import Routes from "./Routes";
import { useState, useEffect } from 'react';
import firebase from 'firebase';

// Configure Firebase.
var config = {
  apiKey: process.env.REACT_APP_API_KEY,
  authDomain: process.env.REACT_APP_AUTHDOMAIN,
  databaseURL: process.env.REACT_APP_BASEURL,
  projectId: process.env.REACT_APP_PROJECT_ID,
  storageBucket: process.env.REACT_APP_STORAGEBUCKET,
  messagingSenderId: process.env.REACT_APP_MESSAGING_SENDER_ID,
  appId: process.env.REACT_APP_APP_ID
};

firebase.initializeApp(config);

function App() {

  const [user, setUser] = useState();
  const [initializing, setInitializing] = useState(true);

  useEffect(() => {
    const subscriber = firebase.auth().onAuthStateChanged(
      (_user) => {
        setUser(_user);
        setInitializing(false);
      }
    );
    return subscriber;
  }, []);


  return (
    <div className="App">

      <a href="/">Polls</a>
      {!initializing ?
        user ?
          <a href="/" onClick={() => {firebase.auth().signOut();}}>Log out</a>
        :
          <a href="/login">Log in</a>
      :
      <div></div>  
      }
      <Routes />
    </div>
  );
}

export default App;
