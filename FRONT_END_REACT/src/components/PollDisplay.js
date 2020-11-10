import React from 'react';
import PollService from '../services/PollService';
import firebase from 'firebase';
import { withRouter } from "react-router";
import Countdown from 'react-countdown';

class PollDisplay extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            poll: null,
            pollId: null,
            user: null,
            votes: [],
            initializing: true
        }
    }

    getPollData = (id) => {
        PollService.getPoll(id).then((response) => {
            console.log("RESPONSE:",response);
            response.status === 200 ?
                this.setState({ poll: response.data, pollId : id, votes: response.data.votes, initializing: false })
            :
                console.log(response);
                this.setState({ initializing: false })
            }).catch((error) => {
                console.log(error);
                this.setState({ initializing: false })
        });
            
    }

    componentDidMount() {
        const id = this.props.match.params.id; 
        id ? this.getPollData(id) : this.setState({ initializing: false });
    }

    getVoteCount = (color) => {
        let red = 0;
        let green = 0;
        this.state.votes.forEach((vote) => {
            vote.result === 1 ? green++ : red++;
        });
        if (color === 'green') {
            return green;
        } else {
            return red;
        }
    }

    getTimeRemaining = () => {
        if (this.state.poll.endDate) {
            return <Countdown date={this.state.poll.endDate} />
        } else {
            return <p>There is no end time set for this poll.</p>
        }
    }

    render() {
        if (this.state.initializing) {
            return (
                <div></div>
            )
        }

        if (!this.state.initializing && this.state.poll) {
            return (
                <div className="row justify-content-around">
                    <div className="col-xs-6 ">
                    <h1>{this.state.poll.title}</h1>
                    <h3>{this.state.poll.description}</h3>
                    <table className="table table-striped">
                        <tbody>
                        <tr>
                            <thead>Option 1</thead>
                            <td>{this.state.poll.red}</td>
                            <td>{this.getVoteCount('red')} vote(s)</td>
                        </tr>
                        <tr>
                            <thead>Option 2</thead>
                            <td>{this.state.poll.green}</td>
                            <td>{this.getVoteCount('green')} vote(s)</td>
                        </tr>
                        <tr>
                            <thead>Time remaining:</thead>
                            <td>{this.getTimeRemaining()}</td>
                        </tr>
                        </tbody>
                    </table>
                    </div>
                </div>
            )
        }
        
        if (!this.state.initializing && !this.state.poll) {
            return (
                <div><p>You need to provide an id for a poll as parameter.</p></div>
            )
        }
    }
}

export default withRouter(PollDisplay);