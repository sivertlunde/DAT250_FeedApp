import React from 'react';
import Cookies from 'universal-cookie';
import PollService from '../services/PollService';
import UserService from '../services/UserService';
import firebase from 'firebase';
import { withRouter } from "react-router";


const cookies = new Cookies();
// Configure Firebase.

function CodeInput(props) {

    const handleSubmit = (e) => {
        e.preventDefault();
        const code = e.target.code.value;
        props.props.history.push("/" + code);
    }

    return (
        <form className="input-group col-sm-5 mb-3" onSubmit={handleSubmit}>
            <div className="input-group-prepend">
                <button className="btn btn-outline-secondary" >Go</button>
            </div>
            <input id="code" type="number" className="form-control form-control-lg" placeholder="Pollcode" aria-label="" aria-describedby="basic-addon1"></input>
        </form>
    )
}

class PollComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            polls: [],
            votes: [],
            user: null
        };
    }

    getPollsAndVotes = () => {
        firebase.auth().currentUser.getIdToken(false).then((token) => {
            UserService.getMyUser(token).then((response) => {
                console.log("UserResponse: ",response);
                this.setState({ polls: response.data.polls, votes: response.data.votes })
                console.log(this.state.polls);
            }).catch((error) => {
              console.log(error);
            })
          }).catch((error) => {
            console.log(error);
          })
    }

    componentDidMount() {
        this.unregisterAuthObserver = firebase.auth().onAuthStateChanged(
            (_user) => {
                if(_user) {
                    this.getPollsAndVotes();
                }
                this.setState({ user: _user });
                console.log("user: ", _user);
                console.log(cookies);
            }
        );
    }

    componentWillUnmount() {
        this.unregisterAuthObserver();
    }

    

    getPollStatus = (index) => {
        let now = new Date();
        let endDate;
        this.state.polls[index].endDate ? endDate = new Date(this.state.polls[index].endDate) : endDate = null;
        if (!this.state.polls[index].startDate) {
            return "Created";
        } else if (!endDate) {
            return "Started";
        } else if (now > endDate) {
            return "Closed";
        } else {
            return "Ending \n" + endDate.toLocaleString();
        }
    }

    render() {
        console.log("currentuser:", firebase.auth().currentUser);
        console.log("cookie: ", cookies.get("user"));
        console.log("render user state: ", this.user);


        if (this.state.user) {
            return (

                <div className="container">
                    <CodeInput props={this.props} />
                    <div className="row justify-content-around">
                        <div className="col-xs-6 ">
                            <h1 className="text-center">Your Polls</h1>
                            <table className="table table-striped">
                                <thead>
                                    <tr>
                                        <td></td>
                                        <td>Title</td>
                                        <td>Status</td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.state.polls ?
                                        this.state.polls.map(
                                            (poll, index) => {
                                                <tr key={poll.id}>
                                                    {console.log("IS IT HERE??", this.getPollStatus(index))}
                                                    <td> <a href={"/poll/" + poll.id}>Edit</a></td>
                                                    <td> {poll.title}</td>
                                                    <td> {this.getPollStatus(index)}</td>
                                                    <td> <a href={"/vote/" + poll.id}>Vote here</a></td>
                                                    <td> <a href={"/display/" + poll.id}>View poll</a></td>
                                                    <td> <a href={"/time/" + poll.id}>Open/Close poll</a></td>
                                                </tr>
                                            })
                                        :
                                        <div><p>Ingen polls</p></div>
                                    }
                                </tbody>
                            </table>
                        </div>
                        
                        <div className="col-xs-6 ">
                            <h1>Your votes</h1>
                            <table className="table table-striped">
                                <thead>
                                    <tr>
                                        <td>Poll</td>
                                        <td>Red vote</td>
                                        <td>Green vote</td>
                                        <td>Your vote</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    {console.log(this.state.votes)}
                                        {this.state.votes ?
                                        this.state.votes.map(
                                            vote =>
                                                <tr key={vote.id}>
                                                    <td> {vote.poll.title}</td>
                                                    <td> {vote.poll.red}</td>
                                                    <td> {vote.poll.green}</td>
                                                    <td> {vote.result === 1 ? vote.poll.green : vote.poll.red}</td>
                                                </tr>
                                        )
                                        :
                                        <div></div>
                                    }
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            )
        }

        return (
            <div className="container">
                <CodeInput props={this.props} />
            </div>

        )
    }
}

export default withRouter(PollComponent)