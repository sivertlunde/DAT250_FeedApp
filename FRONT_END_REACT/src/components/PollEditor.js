import React from 'react';
import PollService from '../services/PollService';
import firebase from 'firebase';

class PollEditor extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            poll: { title: "", description: "", green: "", red: "", isPublic: true },
            user: null,
            initializing: true
        }
    }

    getPollData = (id) => {
        PollService.getPoll(id).then((response) => {
            console.log(response.data);
            this.setState({ poll: response.data })
        })
            .catch((error) => {
            });
    }

    componentDidMount() {
        this.props.id ? this.getPollData(this.props.id) : console.log("It's new");
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

    handleTitleChange = (event) => {
        let newPoll = this.state.poll;
        newPoll.title = event.target.value;
        this.setState({ poll: newPoll });
        console.log(this.state.poll);
    }

    handleDescriptionChange = (event) => {
        let newPoll = this.state.poll;
        newPoll.description = event.target.value;
        this.setState({ poll: newPoll });
    }

    handleOption1Change = (event) => {
        let newPoll = this.state.poll;
        newPoll.green = event.target.value;
        this.setState({ poll: newPoll });
    }

    handleOption2Change = (event) => {
        let newPoll = this.state.poll;
        newPoll.red = event.target.value;
        this.setState({ poll: newPoll });
    }

    handlePublicChange = () => {
        let newPoll = this.state.poll;
        newPoll.isPublic = !this.state.poll.isPublic;
        this.setState({ poll: newPoll });
    }

    handleCreateBtn = () => {
        firebase.auth().currentUser.getIdToken(true).then(function(token) {
            PollService.postPoll(token, this.state.poll).then((response) => {
                console.log(response.data);
            })
            .catch((error) => {
                console.log(error);
            });
        })
        .catch((error) => {
            console.log(error);
        });
    }

    handleUpdateBtn = () => {
        firebase.auth().currentUser.getIdToken(true).then(function(token) {
            PollService.putPoll(this.state.id, token, this.state.poll).then((response) => {
                console.log(response.data);
            })
            .catch((error) => {
                console.log(error);
            });
        })
        .catch((error) => {
            console.log(error);
        });
    }

    render() {
        if (this.state.initializing) {
            return (
                <div></div>
            )
        }

        if (!this.state.initializing && this.state.user) {
            return (
                <form>
                    <h1>{this.props.id ? "Edit Poll" : "New Poll"}</h1>
                    Poll title: <p><input type="text" value={this.state.poll.title} onChange={this.handleTitleChange} /></p>
                    Description: <p><input type="textarea" value={this.state.poll.description} onChange={this.handleDescriptionChange} /></p>
                    Option 1: <p><input type="text" value={this.state.poll.green} onChange={this.handleOption1Change} /></p>
                    Option 2: <p><input type="text" value={this.state.poll.red} onChange={this.handleOption2Change} /></p>
    
                    <div>
                        Public <input type="radio" value="public" name="visibility" checked={this.state.poll.isPublic} onChange={this.handlePublicChange} />
                        Private <input type="radio" value="private" name="visibility" checked={!this.state.poll.isPublic} onChange={this.handlePublicChange} />
                    </div>
                    {this.props.id ?
                        <button onClick={this.handleUpdateBtn}>Update</button>
                    :
                        <button onClick={this.handleCreateBtn}>Create</button>
                    }
                </form>
            )
        }
        
        if (!this.state.initializing && !this.state.user) {
            return (
                <div><p>You need to be signed in to access this page.</p></div>
            )
        }
    }
}

export default PollEditor;