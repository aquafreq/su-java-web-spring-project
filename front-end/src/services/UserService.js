import axios from 'axios'

axios.defaults.withCredentials = true

const userService = {
    login: (username, password) => {
        return axios.post('/user/login', {username, password})
    },
    register: (username, password, email) => {
        return axios.post('/user/register', {username, password, email})
    },
    getCurrentUser: (token) => {
        if (token)
            return axios.get( "/user/",
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
    savePassword: (url, password) => axios.patch(url, password),
    userDetails : pathname => axios.get('admin/'+ pathname),
    fetchUserCategoryWords: url => axios.get(url),
}

export default userService
