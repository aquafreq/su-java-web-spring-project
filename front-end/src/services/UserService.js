import axios from 'axios'

axios.defaults.withCredentials = true

const BASE_URL_PATH = '/auth'
const LOGIN = '/login'
const REGISTER = '/register'
const LOGOUT = '/logout'

const userService = {
    login: (username, password) => {
        return axios.post(BASE_URL_PATH + LOGIN, {username, password})
    },
    register: (username, password, email) => {
        return axios.post(BASE_URL_PATH + REGISTER, {username, password, email})
    },
    // logout: () => {
    //     return axios.post(BASE_URL_PATH + LOGOUT, {})
    // },
    createWord: (word, definition) => {
        return axios.post("/users/create-word")
    },
    getCurrentUser: (token) => {
        if (token)
            return axios.get(BASE_URL_PATH + "/user",
                {
                    headers: {
                        "Authorization": token
                    }
                }
            )
    },
    fetchAllUsers: () => axios.get('/admin/user/all'),
    fetchAllRoles: () => axios.get('/admin/role/all'),
    updateUser: url => axios.patch(url),
    userProfile: url => axios.get(url),
}

export default userService
