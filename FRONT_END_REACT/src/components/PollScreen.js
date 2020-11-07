import React from 'react';




class PollScreen extends React.Component {
    componentDidMount() {
        PollService.getPoll(40).then((response) => {
            this.setState({ poll: response.data })
        });
    }

    render(){
        return(
            <div>
                <h1>Poll</h1>


            </div>
        )
    }
}

export default PollScreen;