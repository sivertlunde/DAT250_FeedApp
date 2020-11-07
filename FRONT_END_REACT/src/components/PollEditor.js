import React from 'react';
import PollService from '../services/PollService'

class PollEditor extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            "poll": {}
        }
    }

    componentDidMount() {
        PollService.getPoll(5).then((response) => {
            this.setState({ "poll": response.data })
        })
            .catch((error) => {
            });
    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }

    render() {
        return (
            <form>
                <h1>Poll Editor</h1>
                Poll name: <p><input type="text" value={this.state.poll.title} onChange={this.handleChange} /></p>
                Description: <p><input type="textarea" value={this.state.poll.description} onChange={this.handleChange} /></p>
                Option 1: <p><input type="text" value={this.state.poll.green} onChange={this.handleChange} /></p>
                Option 2: <p><input type="text" value={this.state.poll.red} onChange={this.handleChange} /></p>

                End date: <p><input type="date" value={this.state.poll.endDate} onChange={this.handleChange} /></p>

                <div>
                    Public <input type="radio" value="private" name="visibility" defaultChecked={ this.state.poll.isPublic} onChange={this.handleChange} />
                    Private <input type="radio" value="public" name="visibility" defaultChecked={!this.state.poll.isPublic} onChange={this.handleChange} />
                </div>
                <button>Create</button>
            </form>
        )
    }
}

export default PollEditor;