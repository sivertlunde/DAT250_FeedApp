import axios from 'axios'

const USERS_REST_API_URL = 'http://localhost:8080/users';

class UserService {

    getUsers() {
        return axios.get(USERS_REST_API_URL, { validateStatus: false });
    }

    getUser(id) {
        return axios.get(USERS_REST_API_URL+'/'+id, { validateStatus: false });
    }

    postUser(userData) {
        axios.post(USERS_REST_API_URL, userData);
    }

    putUser(id, userData) {
        axios.put(USERS_REST_API_URL+'/'+id, userData);
    }
}

export default new UserService();