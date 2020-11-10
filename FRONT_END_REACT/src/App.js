import logo from './logo.svg';
import './App.css';
import Routes from "./Routes";
import { useState, useEffect } from 'react';
import firebase from 'firebase';
import UserService from './services/UserService';

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
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    const subscriber = firebase.auth().onAuthStateChanged(
      (_user) => {
        if (_user) {
          firebase.auth().currentUser.getIdToken(false).then((token) => {
            UserService.getMyUser(token).then((response) => {
              response.data.role.role === "Admin" ? setIsAdmin(true) : console.log(response);
            }).catch((error) => {
              console.log(error);
            })
          }).catch((error) => {
            console.log(error);
          })
        } else {
          setIsAdmin(false);
        }
        setUser(_user);
        setInitializing(false);
      }
    );
    return subscriber;
  }, []);


  return (
    <div className="App">

      <a className="App-link" href="/">Polls</a>
      {!initializing ?
        user ?
          <a className="App-link" href="/" onClick={() => {firebase.auth().signOut();}}>Log out</a>
        :
          <a className="App-link" href="/login">Log in</a>
      :
      <div></div>  
      }
      {isAdmin ?
        <a className="App-link" href="/admin">Admin</a>
      :
        <div></div>
      }
      <Routes />
    </div>
  );
}

export default App;
