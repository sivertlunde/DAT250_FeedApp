import React from "react";
import { Route, Switch } from "react-router-dom";
import PollComponent from './components/PollComponent';
import Login from './components/Login';
import PollEditor from './components/PollEditor';
import PollScreen from './components/PollScreen';
import PollDisplay from './components/PollDisplay';



export default function Routes() {
    return (
      <Switch>
        <Route exact path="/">
          <PollComponent />
        </Route>
        <Route path="/login">
            <Login />
        </Route>
        <Route path="/vote/:id">
            <PollScreen />
        </Route>
        <Route path="/poll/:id">
            <PollEditor />
        </Route>
        <Route path="/poll">
            <PollEditor />
        </Route>
        <Route path="/vote">
            <PollScreen />
        </Route>
        <Route path="/display/:id">
            <PollDisplay />
        </Route>
        <Route path="/display/">
            <PollDisplay />
        </Route>
      </Switch>
    );
  }
  