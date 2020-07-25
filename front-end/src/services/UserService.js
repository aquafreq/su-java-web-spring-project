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
    updateUser: (url, user) => axios.patch(url, user),
    userProfile: url => axios.get(url),
    createCategoryForUser: (userId, category) =>
        axios.post(`/user/profile/${userId}/create-category`, category),
    createWordForCategory: (userId, categoryId, word) =>
        axios.post(`/user/profile/${userId}/${categoryId}/create-word`, word),
    fetchUserCategories: (id) => axios.get(`/user/profile/${id}/categories`),
    savePassword: (url, password) => axios.patch(url, password)
}

export default userService
