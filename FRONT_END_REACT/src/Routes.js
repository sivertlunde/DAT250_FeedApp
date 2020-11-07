import React from "react";
import { Route, Switch } from "react-router-dom";
import PollComponent from './components/PollComponent';
import Login from './components/Login';
import PollEditor from './components/PollEditor';
import PollScreen from './components/PollScreen';



export default function Routes() {
    return (
      <Switch>
        <Route exact path="/">
          <PollComponent />
        </Route>
        <Route path="/login">
            <Login />
        </Route>
        <Route path="/vote">
            <PollScreen />
        </Route>
        <Route path="/createPoll">
            <PollEditor />
        </Route>
        <Route path="/:id">
            <PollScreen />
        </Route>
      </Switch>
    );
  }
  