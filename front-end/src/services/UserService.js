import axios from 'axios'

const BASE_URL_PATH = '/api/auth'
const LOGIN = '/login'
const REGISTER = '/register'
const LOGOUT = '/logout'

const userService = {

    login: (username, password) => {
        return axios.post(BASE_URL_PATH + LOGIN, {username, password},
            {withCredentials:true})
            .then(resp => {
                let headers = resp.headers;

                localStorage.setItem("Authorization", resp.headers.authorization)
                debugger
                return resp;
            }, console.log)
    },
    register: (username, password, email) => {
        return axios
            .post(BASE_URL_PATH + REGISTER, {username, password, email})
            .then(resp => {
                debugger
            })
            .catch(console.log)
    },
    logout: () => {
        return axios.post(BASE_URL_PATH + LOGOUT, {})
            .then(resp => {
                localStorage.clear();
            }, console.log)
    },
}

export default userService