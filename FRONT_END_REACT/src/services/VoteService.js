import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/votes';

class VoteService {
    postVote(pollId, userId, vote) {
        axios.post(POLLS_REST_API_URL+"?voterId="+userId+"&pollId="+pollId+"&vote="+vote);
    }

    postVote(pollId, vote) {
        axios.post(POLLS_REST_API_URL+"?pollId="+pollId+"&vote="+vote);
    }
}

export default new VoteService();