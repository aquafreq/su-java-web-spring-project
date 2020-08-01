import axios from 'axios'

axios.defaults.withCredentials = true

const userService = {
    login: (username, password) => {
        return axios.post('/auth/login', {username, password})
    },
    register: (username, password, email) => {
        return axios.post('/auth/register', {username, password, email})
    },
    getCurrentUser: (token) => {
        if (token)
            return axios.get("/auth",
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
    userDetails: pathname => axios.get('admin' + pathname),
    fetchUserCategoryWords: url => axios.get(url),
    deleteWordFromCategory: (url, name, wordName) => axios.delete(url+'/delete-word', {data: {name, wordName}}),
    deleteCategory: (url, id) => axios.delete(url+ '/delete-category', {data: {id}}),
}

export default userService
