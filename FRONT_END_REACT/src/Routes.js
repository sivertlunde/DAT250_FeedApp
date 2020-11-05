import React from "react";
import { Route, Switch } from "react-router-dom";
import PollComponent from './components/PollComponent';
import Login from './components/Login';



export default function Routes() {
    return (
      <Switch>
        <Route exact path="/">
          <PollComponent />
        </Route>
        <Route path="/login">
            <Login />
        </Route>
      </Switch>
    );
  }
  