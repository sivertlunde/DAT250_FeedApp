import axios from 'axios'

const VOTES_REST_API_URL = 'http://localhost:8080/votes';

class VoteService {
    postVote(pollId, vote, userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.post(VOTES_REST_API_URL, null, {headers: header, params: {pollId: pollId, vote: vote}});
    }

    postAnonVote(pollId, vote) {
        return axios.post(VOTES_REST_API_URL, null, {params: {pollId: pollId, vote: vote}});
    }

    getVotes(userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.get(VOTES_REST_API_URL, { headers: header });
    }
}

export default new VoteService();