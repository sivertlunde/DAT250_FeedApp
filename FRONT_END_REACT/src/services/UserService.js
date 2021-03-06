import axios from 'axios'

const USERS_REST_API_URL = 'http://localhost:8080/users';

class UserService {

    getUsers() {
        return axios.get(USERS_REST_API_URL, { validateStatus: false });
    }

    getUser(id) {
        return axios.get(USERS_REST_API_URL+'/'+id, { validateStatus: false });
    }

    getMyUser(userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.get(USERS_REST_API_URL+'/me', { validateStatus: false, headers: header });
    }

    postUser(userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.post(USERS_REST_API_URL, null, { headers: header });
    }

    putUser(id, userData) {
        return axios.put(USERS_REST_API_URL+'/'+id, userData);
    }

    deleteUser(id, userToken) {
        const header = { Authorization: `Bearer ${userToken}` };
        return axios.delete(USERS_REST_API_URL+'/'+id, { headers: header});
    }
}

export default new UserService();