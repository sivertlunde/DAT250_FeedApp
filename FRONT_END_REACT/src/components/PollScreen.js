import React from 'react';
import PollService from '../services/PollService';
import { useLocation } from 'react-router-dom'

let id;

function Path() {

    const pathname = useLocation().pathname;
    const numberInUrl = /\d+/;

    if(numberInUrl.exec(pathname) !== null) {
        id = pathname.match(numberInUrl)[0]
    }
    return (
        ""
    )
}

class PollScreen extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            "poll": {},
            "vote": ""
        }
    }

    componentDidMount() {
        PollService.getPoll(id).then((response) => {
            this.setState({ "poll": response.data })
        })
            .catch((error) => {
            });
    }

    handleSubmit(event) {
        event.preventDefault();
        alert("Hei");
    }

    handle

    render() {
        if (this.state.poll) {
            return (
                <div>
                    {
                        <form onSubmit={this.handleSubmit}>
                            <Path />
                            <h1>{this.state.poll.title}</h1>
                            <p>{this.state.poll.description}</p>
                            <div className="">
                                <input type="radio" value="green" name="vote" /> {this.state.poll.green}
                                <input type="radio" value="red" name="vote" /> {this.state.poll.red}
                            </div>
                            <button>Vote</button>

                        </form>


                    }
                </div>
            )
        }
        return (
            <div>
                <h1>Could not find poll #{id}</h1>
            </div>
        )
    }
}

export default PollScreen;