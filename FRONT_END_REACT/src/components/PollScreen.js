import React from 'react';
import PollService from '../services/PollService';
import VoteService from '../services/VoteService';
import { useLocation } from 'react-router-dom'
import { withRouter } from "react-router";
import firebase from 'firebase';

class PollScreen extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            "poll": {},
            "vote": ""
        }
        this.onValueChange = this.onValueChange.bind(this);
        this.formSubmit = this.handleSubmit.bind(this);    
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        PollService.getPoll(id).then((response) => {
            this.setState({ "poll": response.data })
        })
            .catch((error) => {
            });
    }

    handleSubmit = (event) => {
        const id = this.props.match.params.id;
        firebase.auth().currentUser.getIdToken(false).then((token) => {
             VoteService.postVote(id, 3, this.state.selectedOption, token);
         })
         .catch((error) => {
             console.log(error);
         });
         event.preventDefault();
        // if(this.state.selectedOption) {
        //     VoteService.postVote(id, this.state.selectedOption)
        // }
        console.log(event.target);
    }

    onValueChange(event) {
        this.setState({
            selectedOption: event.target.value
        });
    }

    render() {
        if (this.state.poll) {
            return (
                <div>
                    {
                        <form onSubmit={this.handleSubmit}>
                            <h1>{this.state.poll.title}</h1>
                            <p>{this.state.poll.description}</p>
                            <div className="">
                                <input type="radio" value="0" name="vote" onChange={this.onValueChange} checked={this.state.selectedOption === "0"} /> {this.state.poll.green}
                                <input type="radio" value="1" name="vote" onChange={this.onValueChange} checked={this.state.selectedOption === "1"} /> {this.state.poll.red}
                            </div>
                            <button>Vote</button>
                        </form>
                    }
                </div>
            )
        }
        return (
            <div>
                <h1>Could not find poll #{this.props.match.params.id}</h1>
            </div>
        )
    }
}

export default withRouter(PollScreen);