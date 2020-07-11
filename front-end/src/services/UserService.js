import axios from 'axios'

const BASE_URL_PATH = '/api/auth'
const LOGIN = '/login'
const REGISTER = '/register'
const LOGOUT = '/logout'
export const jwtToken = localStorage.getItem("authorization")

axios.defaults.withCredentials = true;

const userService = {
    login: (username, password) => {
        return axios.post(BASE_URL_PATH + LOGIN, {username, password})
    },
    register: (username, password, email) => {
        return axios.post(BASE_URL_PATH + REGISTER, {username, password, email})
    },
    logout: () => {
        return axios.post(BASE_URL_PATH + LOGOUT, {})
    },
    createWord: (word, definition) => {
        return axios.post("api/users/create-word")
    },
    getCurrentUser: (token) => {
        return !!token ?
            axios.get(BASE_URL_PATH + "/user",
                {headers: {"Authorization": token}})
            : Promise.reject();
    },
}

export default userService
