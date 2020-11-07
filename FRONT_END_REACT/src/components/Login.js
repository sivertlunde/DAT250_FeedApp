import React from 'react';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import firebase from 'firebase';
import auth from 'firebase/firebase-auth';


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

class Login extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            user: null,
            initializing: true
        };
    }
    componentDidMount() {
        this.unregisterAuthObserver = firebase.auth().onAuthStateChanged(
            (_user) => {
                this.setState({ user: _user, initializing: false });
                console.log("user: ", _user);
            }
        );
    }

    componentWillUnmount() {
        this.unregisterAuthObserver();
    }

    render() {
        if (this.state.initializing) {
            return (
                <div></div>
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
