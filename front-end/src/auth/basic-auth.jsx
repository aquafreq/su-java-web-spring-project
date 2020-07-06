export const AuthenticationService = {
    registerSuccessfulLogin: (username, password) => {
        //let basicAuthHeader = 'Basic ' +  window.btoa(username + ":" + password)
        //console.log('registerSuccessfulLogin')
        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username)
        this.setupAxiosInterceptors(this.createBasicAuthToken(username, password))
    },
    setupAxiosInterceptors: (token) => {
        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token
                }
                return config
            }
        )
    }

    // executeBasicAuthenticationService(username, password) {
    //     return axios.get(`${API_URL}/basicauth`,
    //         {headers: {authorization: this.createBasicAuthToken(username, password)}})
    // }
    //
    // createBasicAuthToken(username, password) {
    //     return 'Basic ' + window.btoa(username + ":" + password)
    // }
}