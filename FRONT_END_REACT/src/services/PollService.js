import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/polls';

class PollService {

    getPolls() {
        return axios.get(POLLS_REST_API_URL, { validateStatus: false });
    }

    getPoll(pollId) {
        return axios.get(POLLS_REST_API_URL+'/'+pollId, { validateStatus: false });
    }

    postPoll(userToken, pollData) {
        axios.post(POLLS_REST_API_URL, {data: pollData}, {headers: { Authorization: `Bearer ${userToken}` }});
    }

    putPoll(pollId, userToken, pollData) {
        axios.put(POLLS_REST_API_URL+'/'+pollId, {data: pollData}, {headers: { Authorization: `Bearer ${userToken}` }});
    }
}

export default new PollService();