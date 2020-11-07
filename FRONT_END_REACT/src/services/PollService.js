import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/polls';

class PollService {

    getPolls() {
        return axios.get(POLLS_REST_API_URL, { validateStatus: false });
    }

    getPoll(id) {
        return axios.get(POLLS_REST_API_URL+'/'+id, { validateStatus: false });
    }

    postPoll(userId, pollData) {
        axios.post(POLLS_REST_API_URL+"?userId="+userId, pollData);
    }
}

export default new PollService();