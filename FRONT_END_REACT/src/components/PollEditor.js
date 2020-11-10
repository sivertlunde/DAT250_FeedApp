import React from 'react';
import PollService from '../services/PollService';
import firebase from 'firebase';
import { withRouter } from "react-router";
import DateTimePicker from 'react-datetime-picker';

class PollEditor extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            poll: { title: "", description: "", green: "", red: "", isPublic: true, startDate: "", endDate: "" },
            pollId: null,
            user: null,
            started: false,
            isClosed: false,
            openNowChecked: false,
            closeNowChecked: false,
            datePickerValue: null,
            storedEndDate: "",
            initializing: true
        }
    }

    getPollData = (id) => {
        PollService.getPoll(id).then((response) => {
            if (response.status === 200) {
                this.setState({ poll: response.data, pollId : id });
                const now = new Date();
                response.data.endDate ? this.setState({ datePickerValue: new Date(response.data.endDate) }) : this.setState({ datePickerValue: null });
                const endDate = this.state.datePickerValue;
                if (endDate && now > endDate) {
                    this.setState({ isClosed: true })
                } else {
                    response.data.startDate ? this.setState({ started: true, storedEndDate: response.data.endDate }) : this.setState({ started: false });
                }
            }
            console.log(response);
        })
            .catch((error) => {
                console.log(error);
            });
    }

    componentDidMount() {
        const id = this.props.match.params.id; 
        id ? this.getPollData(id) : console.log("It's new");
        this.unregisterAuthObserver = firebase.auth().onAuthStateChanged(
            (_user) => {
                this.setState({ user: _user, initializing: false });
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

    handleStartNow = () => {
        this.setState({ openNowChecked: !this.state.openNowChecked})
        let newPoll = this.state.poll;
        if (!this.state.openNowChecked) {
            newPoll.startDate = new Date().toISOString();
            console.log("New Poll", newPoll);
            this.setState({ poll: newPoll });
        } else {
            newPoll.startDate = "";
            newPoll.endDate = "";
            this.setState({ poll: newPoll });
        }
    }

    handleCloseNow = () => {
        this.setState({ closeNowChecked: !this.state.closeNowChecked})
        let newPoll = this.state.poll;
        if (!this.state.closeNowChecked) {
            newPoll.endDate = new Date();
            this.setState({ poll: newPoll });
        } else {
            newPoll.endDate = this.state.storedEndDate;
            this.setState({ poll: newPoll });
        }
        
    }

    handleChangeEndTime = (newDate) => {
        let endDate = new Date(newDate);
        this.setState({ datePickerValue: endDate})
        console.log(endDate);
        let newPoll = this.state.poll;
        newPoll.endDate = endDate.toISOString();
        console.log(newPoll);
        this.setState({ poll: newPoll });
    }

    handleCreateBtn = () => {
        console.log(this.state.poll);
        firebase.auth().currentUser.getIdToken(false).then((token) => {
            console.log(token);
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
        console.log("Poll before update: ",this.state.poll.startDate);
        firebase.auth().currentUser.getIdToken(false).then((token) => {
            console.log(token);
            PollService.putPoll(this.state.pollId, token, this.state.poll).then((response) => {
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

        if (this.state.isClosed) {
            return(
                <div><p>This poll is closed.</p></div>
            )
        }

        if (!this.state.initializing && this.state.user && !this.state.isClosed) {
            return (
                <form>
                    <h1>{this.state.pollId ? "Edit Poll" : "New Poll"}</h1>
                    Poll title: <p><input type="text" value={this.state.poll.title} onChange={this.handleTitleChange} /></p>
                    Description: <p><input type="textarea" value={this.state.poll.description} onChange={this.handleDescriptionChange} /></p>
                    Option 1: <p><input type="text" value={this.state.poll.green} onChange={this.handleOption1Change} /></p>
                    Option 2: <p><input type="text" value={this.state.poll.red} onChange={this.handleOption2Change} /></p>
    
                    <div>
                        Public <input type="radio" value="public" name="visibility" checked={this.state.poll.isPublic} onChange={this.handlePublicChange} />
                        Private <input type="radio" value="private" name="visibility" checked={!this.state.poll.isPublic} onChange={this.handlePublicChange} />
                    </div>
                    {this.state.started ? 
                        <div>
                            <p>Close poll now <input type="checkbox" name="closeNow" checked={this.state.closeNowChecked} onChange={this.handleCloseNow}/></p>
                                {this.state.closeNowChecked ? 
                                    <div></div> 
                                :
                                    <div><p>Schedule time for closing poll (optional): </p><DateTimePicker onChange={this.handleChangeEndTime} value={this.state.datePickerValue}/></div>
                                } 
                        </div>
                    : 
                        <div>
                            <p>Open poll now <input type="checkbox" name="openNow" checked={this.state.openNowChecked} onChange={this.handleStartNow}/></p>
                                {this.state.openNowChecked ? 
                                    <div><p>Schedule time for closing poll (optional): </p><DateTimePicker onChange={this.handleChangeEndTime} value={this.state.datePickerValue}/></div>
                                :
                                    <div></div>
                                } 
                        </div>  
                    }
                    {this.state.pollId ?
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

export default withRouter(PollEditor);