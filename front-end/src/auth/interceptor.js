const axios = require("axios");

axios.interceptors.request.use(
    config => {
        let item = localStorage.getItem("authorization");

        if (item) {
            config.headers["Authorization"] = item;
        }

        return config;
    },
    err => {
        return Promise.reject(err);
    }
);