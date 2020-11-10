import React from 'react';
import { withRouter } from 'react-router-dom';
import UserService from '../services/UserService';
import VoteService from '../services/VoteService';
import PollService from '../services/PollService';
import firebase from 'firebase';


class AdminScreen extends React.Component {

    constructor(props) {
        super(props);
        console.log("from admin screen", props.isAdmin);
        this.state = {
            polls: [],
            votes: [],
            users: [],
            isAdmin: false
        };
    }

    getAllData = () => {
        Promise.all([
            UserService.getUsers(),
            VoteService.getVotes(),
            PollService.getPolls()
        ]).then(([usersRes, votesRes, pollsRes]) => {
            this.setState({
                polls: pollsRes.data,
                votes: votesRes.data,
                users: usersRes.data
            })
        })
    }

    componentDidMount() {
        firebase.auth().currentUser.getIdToken(false).then((token) => {
            UserService.getMyUser(token).then((response) => {
                response.data.role.role === "Admin" ? this.setState({isAdmin: true}) : console.log(response);
            }).catch((error) => {
                console.log(error);
            })
        }).catch((error) => {
            console.log(error);
        })
        this.getAllData();
    }

    handlePollDelete(event) {
        event.preventDefault();
        if (window.confirm("Are you sure you want to delete this poll? All votes will also be deleted.")) {
            PollService.deletePoll(event.target.id).then((res) => console.log("Delted POLL, and here's the response", res))
        }
    }

    handleUserDelete(event) {
        event.preventDefault();
        if (window.confirm("Are you sure you want to delete this user? All its polls and votes will also be deleted.")) {
            firebase.auth().currentUser.getIdToken(false).then((token) => {
                return UserService.deleteUser(event.target.id, token);
            })
                .then((res) => {
                    console.log("Deleted USER, and here's the response", res)
                })
                .catch((error) => {
                    console.log(error);
                })
        }
    }

    handleVoteDelete(event) {
        event.preventDefault();
        if (window.confirm("Are you sure you want to delete this vote? (This is very undemocratic)")) {
            VoteService.deleteVote(event.target.id).then((res) => console.log("Delted VOTE, and here's the response", res))
            // VoteService.deleteVote()
        }
    }

    render() {
        if (this.state.isAdmin) {
            return (
                <div>
                    <h1>Admin</h1>

                    <div className="container-fluid">
                        <div className="row justify-content-around">
                            <div className="col-xs-6 ">
                                <h1 className="text-center">Polls</h1>
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <td></td>
                                            <td>Title</td>
                                            <td>Description</td>
                                            <td>Start date</td>
                                            <td>End date</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            this.state.polls ?
                                                this.state.polls.map(
                                                    poll =>
                                                        <tr key={poll.id}>
                                                            <td> <a href={"/vote/" + poll.id}>Link</a></td>
                                                            <td> {poll.title}</td>
                                                            <td> {poll.description}</td>
                                                            <td> {poll.startDate}</td>
                                                            <td> {poll.endDate}</td>
                                                            <td> <a href={"/poll/" + poll.id}>Edit</a></td>
                                                            <td> <a id={poll.id} href={""} onClick={this.handlePollDelete}>Delete</a></td>
                                                        </tr>
                                                )
                                                :
                                                <tr></tr>
                                        }
                                    </tbody>
                                </table>
                            </div>

                            <div className="col-xs-6 ">
                                <h1>Votes</h1>
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <td>Poll</td>
                                            <td>Voter</td>
                                            <td>Your vote</td>
                                            <td></td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {console.log(this.state.votes)}
                                        {this.state.votes ?
                                            this.state.votes.map(
                                                vote =>
                                                    <tr key={vote.id}>
                                                        <td> {vote.poll.title}</td>
                                                        <td> {vote.voter.email}</td>
                                                        <td> {vote.result === 1 ? vote.poll.green : vote.poll.red}</td>
                                                        <td> <a id={vote.id} href={""} onClick={this.handleVoteDelete}>Delete</a></td>
                                                    </tr>
                                            )
                                            :
                                            <tr></tr>
                                        }
                                    </tbody>
                                </table>
                            </div>
                            <div className="col-xs-6 ">
                                <h1>Users</h1>
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <td>User ID</td>
                                            <td>Email</td>
                                            <td></td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {console.log(this.state.users)}
                                        {this.state.users ?
                                            this.state.users.map(
                                                user =>
                                                    <tr key={user.id}>
                                                        <td> {user.id}</td>
                                                        <td> {user.email}</td>
                                                        <td> <a id={user.id} href={""} onClick={this.handleUserDelete}>Delete</a></td>
                                                    </tr>
                                            )
                                            :
                                            <tr></tr>
                                        }
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            )
        }
        else {
            return (
                <div>
                    <h1>u r not admin</h1>
                </div>
            )
        }

    }
}

export default withRouter(AdminScreen)