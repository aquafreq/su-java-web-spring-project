const axios = require("axios");

export const jwtToken = localStorage.getItem("authorization");

axios.interceptors.request.use(
    config => {
        if (jwtToken) {
            config.headers["Authorization"] = "Bearer " + jwtToken;
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    }
);