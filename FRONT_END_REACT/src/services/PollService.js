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
        return axios.post(POLLS_REST_API_URL, pollData, {headers: header });
    }

    putPoll(pollId, userToken, pollData) {
        let _pollId = pollId
        const header = { Authorization: `Bearer ${userToken}` };
        const param = { pollId: _pollId };
        const config = { headers: header, params: param }
        return axios.put(POLLS_REST_API_URL, pollData, config);
    }

    deletePoll(pollId) {
        const body = {id: pollId}
        return axios.delete(POLLS_REST_API_URL+"/"+pollId, {body: body});

    }
}

export default new PollService();