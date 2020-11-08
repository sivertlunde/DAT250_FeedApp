import axios from 'axios'

const POLLS_REST_API_URL = 'http://localhost:8080/polls';

class PollService {

    getPolls() {
        return axios.get(POLLS_REST_API_URL, { validateStatus: false });
    }

    getPoll(pollId) {
        return axios.get(POLLS_REST_API_URL+'/'+pollId, { validateStatus: false });
    }
    
    //Har lagt inn (userId: 4) for å teste at det funker for meg. Dette skal fjernes
    postPoll(userToken, pollData) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.post(POLLS_REST_API_URL, pollData, {headers: header, params: {userId: 4}});
    }

    putPoll(pollId, userToken, pollData) {
        axios.put(POLLS_REST_API_URL+'/'+pollId, {data: pollData}, {headers: { Authorization: `Bearer ${userToken}` }});
    }
}

export default new PollService();