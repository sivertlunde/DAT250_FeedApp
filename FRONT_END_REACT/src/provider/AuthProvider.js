import React from 'react';

const AuthProvider = (props) => {
    return (
        <firebaseAuth.Provider
            value={{
                test: "context is working"
            }}>
            {props.children}

        </firebaseAuth.Provider>
    );
};

export default AuthProvider;

export const firebaseAuth = React.createContext()
