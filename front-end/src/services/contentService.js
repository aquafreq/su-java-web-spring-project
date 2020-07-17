import axios from 'axios'

export default {
    fetchCategories: () => {
        return axios.get('/api/content/category/all')
    },
    createCategory: (category) => {
        return axios.post('/api/content/category/create', {name: category})
    },
    createExercise: (exercise) => {
        return axios.post('/api/content/exercise/create', {name: exercise})
    }
}