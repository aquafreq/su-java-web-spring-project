import axios from 'axios'

export default {
    fetchCategories: () => {
        return axios.get('/content/all/categories')
    },
    addCategory: (category) => {
        return axios.post('/content/create/category', {name: category})
    }

}