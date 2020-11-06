import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/polls';

class PollService {

    getPolls() {
        return axios.get(POLLS_REST_API_URL);
    }
}

export default new PollService();