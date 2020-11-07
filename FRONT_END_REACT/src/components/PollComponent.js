import React from 'react';
import Cookies from 'universal-cookie';
import PollService from '../services/PollService';
import firebase from 'firebase';

const cookies = new Cookies();
// Configure Firebase.

function CodeInput() {
    return (
        <div className="input-group col-sm-5 mb-3">
            <div className="input-group-prepend">
                <button className="btn btn-outline-secondary" type="button">Go</button>
            </div>
            <input type="text" className="form-control" placeholder="Pollcode" aria-label="" aria-describedby="basic-addon1"></input>
        </div>
    )
}

class PollComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            polls: []
        }
    }

    componentDidMount() {
        PollService.getPolls().then((response) => {
            this.setState({ polls: response.data })
        });
    }

    render() {
        console.log("currentuser:", firebase.auth());


        if (cookies.get("user") !== null) {
            return (

                <div>
                    <CodeInput/>
                    <h1 className="text-center"> Polls</h1>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <td>Poll id</td>
                                <td>Title</td>
                                <td>Description</td>
                                <td>Start date</td>
                                <td>End date</td>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.polls.map(
                                    poll =>
                                        <tr key={poll.id}>
                                            <td> {poll.id}</td>
                                            <td> {poll.title}</td>
                                            <td> {poll.description}</td>
                                            <td> {poll.startDate}</td>
                                            <td> {poll.endDate}</td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            )
        }

        return (
            <CodeInput/>
        )
    }
}

export default PollComponent