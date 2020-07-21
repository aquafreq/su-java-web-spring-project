import axios from 'axios'
import {trackPromise} from "react-promise-tracker";

export default {
    fetchCategories: () => {
            return axios.get('/content/category/all')
    },
    createCategory: (category) => {
        return axios.post('/content/category/create', {name: category})
    },
    createExercise: (exercise) =>
        axios.post('/content/exercise/create', {name: exercise}),
    createContent: (data) => axios.post('/content/add', data),
    getCategoryContent: (id) => axios.get('/content' + id),
    getContent: (url) => axios.get('/content' + url),

    addCommentToContent: (id, comment) => {
        return axios.post(`/content${id}`, comment)
    }
}