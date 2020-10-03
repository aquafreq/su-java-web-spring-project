import axios from 'axios'

export default {
    fetchCategories: () => axios.get('/content/category/all-categories'),
    fetchGrammarLevels: () => axios.get('/content/category/levels'),
    createCategory: (category) => axios.post('/content/category/create', {name: category}),
    createContent: (data) => axios.post('/content/create', data),
    getCategoryContent: (id) => axios.get('/content' + id),
    getContent: (url) => axios.get('/content' + url),
    addCommentToContent: (id, comment) => axios.post(`/content${id}`, comment),
}