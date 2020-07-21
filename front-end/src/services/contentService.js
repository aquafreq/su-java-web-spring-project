import axios from 'axios'
import {trackPromise} from "react-promise-tracker";

export default {
    fetchCategories: () => {
            return axios.get('/api/content/category/all')
    },
    createCategory: (category) => {
        return axios.post('/api/content/category/create', {name: category})
    },
    createExercise: (exercise) =>
        axios.post('/api/content/exercise/create', {name: exercise}),
    createContent: (data) => axios.post('/api/content/add', data),
    getCategoryContent: (id) => axios.get('/api/content' + id),
    getContent: (url) => axios.get('/api/content' + url),

    addCommentToContent: (id, comment) => {
        return axios.post(`/api/content${id}`, comment)
    }
}