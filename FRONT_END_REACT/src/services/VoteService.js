import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/votes';

class VoteService {
    postVote(pollId, userId, vote, userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.post(POLLS_REST_API_URL, null, {headers: header, params: {pollId: pollId, voterId: userId, vote: vote}});
    }

    postAnonVote(pollId, vote) {
        axios.post(POLLS_REST_API_URL+"?pollId="+pollId+"&vote="+vote);
    }
}

export default new VoteService();