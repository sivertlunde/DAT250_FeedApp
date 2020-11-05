import React from 'react';
import PollService from '../services/PollService';

class PollComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            polls:[]
        }
    }

    componentDidMount() {
        PollService.getPolls().then((response) => {
            this.setState({ polls: response.data})
        });
    }

    render () {
        return (
            <div>
                <h1 className = "text-center"> Polls</h1>
                <table className = "table table-striped">
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
                                <tr key = {poll.id}>
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
}

export default PollComponent