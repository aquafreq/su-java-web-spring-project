import axios from 'axios'

export default {
    fetchCategories: () => {
            return axios.get('/content/category/all-categories')
    },
    fetchGrammarLevels: () => {
            return axios.get('/content/category/levels')
    },

    createCategory: (category) => {
        return axios.post('/content/category/create', {name: category})
    },
    createExercise: (exercise) =>
        axios.post('/content/exercise/create', {name: exercise}),
    createContent: (data) => axios.post('/content/create', data),
    getCategoryContent: (id) => axios.get('/content' + id),
    getContent: (url) => axios.get('/content' + url),

    addCommentToContent: (id, comment) => {
        return axios.post(`/content${id}`, comment)
    }
}