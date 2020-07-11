import React, {useEffect, useLayoutEffect, useState} from 'react'
import {Redirect, Route, Switch, useHistory} from 'react-router-dom'
import Navigation from './components/Navigation/Navigation'
import Footer from './components/Footer/Footer'
import Home from './components/Home/Home'
import Login from './components/User/Login/Login'
import Register from './components/User/Register/Register'
import Logout from './components/User/Logout/Logout'
import userService from "../src/services/UserService"
import NotFound from "./components/NotFound/NotFound"
import CreateWord from "./components/User/Profile/CreateWord";
import {Loading} from "./components/Loading/Loading";
import AuthenticatedRoute from "./components/Routes/AuthenticatedRoute";
import UnauthenticatedRoute from "./components/Routes/UnauthenticatedRoute";
import CreateContent from "./components/AdminPanel/CreateContent/CreateContent";
import ManageRole from "./components/AdminPanel/ManageRole";

const App = () => {
    const [isAuthenticated, setisAuthenticated] = useState(!!localStorage.getItem("authorization"))
    const [isLoading, setIsLoading] = useState(true)
    const [user, setUser] = useState({username: '', id: '', authorities: []})
    const history = useHistory()

    useLayoutEffect(() => {
        let token = localStorage.getItem("authorization");

        (async () => {
            try {
                let axiosResponse = await userService.getCurrentUser(token)
                setUserState(axiosResponse.data)
            } catch (e) {
                setUserState({id: '', username: '', authorities: []})
            }
        })()

        // setTimeout(() => setIsLoading(false), 1000)
        setIsLoading(false)
    }, [isAuthenticated])

    const setUserState = (user) => {
        setUser({...user})
    }

    const login = async (username, password) => {
        return await userService.login(username, password)
            .then((r) => {
                setIsLoading(true)
                localStorage.setItem("authorization", r.headers.authorization)
                setisAuthenticated(true)
                history.push('/')
            }, e => e);
    }


    const logout = () => {
        localStorage.clear()
        setisAuthenticated(false)
        history.push('/')
    }

    const register = async (username, password, email) => {
        return await userService.register(username, password, email)
            .then(() => history.push('/login'), (err) => err)
    }

    return (
        isLoading ? <Loading/> :
            <div className="App">
                <Navigation user={user} logout={logout} />
                <Switch>
                    <Route path="/" exact component={Home}/>
                    <Route path="/login" render={() => (!isAuthenticated ?
                        <Login login={login}/> : <Redirect to="/"/>)}/>
                    <Route path="/register" render={() => (!isAuthenticated ?
                        <Register register={register}/> : <Redirect to="/"/>)}/>
                    <Route path="/logout" render={() => (isAuthenticated ?
                            <Logout logout={logout}/> : <Redirect to="/"/>
                    )}/>

                    <AuthenticatedRoute
                        path="/administration/create-content"
                        component={CreateContent}
                        appProps={{isAuthenticated}}
                    />
                    <AuthenticatedRoute
                        path="/administration/manage-roles"
                        component={ManageRole}
                        appProps={{isAuthenticated}}
                    />

                    <Route path="/create-word" component={CreateWord}/>
                    <Route path="*">
                        <NotFound/>
                    </Route>
                </Switch>
                <Footer/>
            </div>
    )
}

export default App