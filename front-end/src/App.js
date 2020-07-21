import React, {Fragment, createContext, useCallback, useContext, useEffect, useLayoutEffect, useState} from 'react'

import {BrowserRouter, Redirect, Route, Switch, useHistory} from 'react-router-dom'

import styles from './App.module.css'
import Navigation from './components/Navigation/Navigation'
import Footer from './components/Footer/Footer'
import Home from './components/Home/Home'
import Login from './components/User/Login/Login'
import Register from './components/User/Register/Register'
import Logout from './components/User/Logout/Logout'
import userService from "../src/services/UserService"
import NotFound from "./components/NotFound/NotFound"
import CreateWord from "./components/User/Profile/CreateWord"
import {Loading} from "./components/Loading/Loading"
import AuthenticatedRoute from "./components/Routes/AuthenticatedRoute"
import UnauthenticatedRoute from "./components/Routes/UnauthenticatedRoute"
import CreateContent from "./components/AdminPanel/CreateContent/CreateContent"
import ManageRole from "./components/AdminPanel/ManageRole"
import UserContext from "./auth/UserContext"
import Profile from "./components/User/Profile/Profile"
import AllContent from "./components/AdminPanel/Content/AllContent";

import Content from "./components/AdminPanel/Content/Content";

const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("authorization"))
    const [isLoading, setIsLoading] = useState(true)
    const [user, setUser] = useState({username: '', id: '', authorities: []})
    const history = useHistory()

    useLayoutEffect(() => {
        let token = localStorage.getItem("authorization");
        (async () => {
            try {
                let axiosResponse = await userService.getCurrentUser(token)
                localStorage.setItem('userId', axiosResponse.data.id)
                setUserState(axiosResponse.data)
                setIsLoading(false)
            } catch (e) {
                setUserState({id: '', username: '', authorities: []})
                setIsLoading(false)
            }
        })()
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
                setIsAuthenticated(true)
                history.push('/')
            }, e => e)
    }

    const register = async (username, password, email) => {
        return await userService.register(username, password, email)
            .then(() => history.push('/login'), (err) => err)
    }

    const userRoles = (auth) => user.authorities.map(u => u.authority).includes(auth)

    const logout = () => {
        localStorage.clear()
        setIsAuthenticated(false)
        history.push('/')
    }

    // debugger
    return (
        <Fragment>
            <BrowserRouter>
                {isLoading ? <Loading/> : (
                    <div className={styles.app}>
                        <UserContext.Provider
                            value={{
                                username: user.username,
                                id: user.id,
                                userRoles: user.authorities.map(r => r.authority)
                            }}>
                            <Switch>
                                <Route path="/" exact isAuthenticated={isAuthenticated} component={Home}/>
                                <Route path="/login" exact render={() => (!isAuthenticated ?
                                    <Login login={login}/> : <Redirect to="/"/>)}/>
                                <Route path="/register" render={() => (!isAuthenticated ?
                                    <Register register={register}/> : <Redirect to="/"/>)}/>
                                <Route path="/logout" render={() => (isAuthenticated ?
                                        <Logout logout={logout}/> : <Redirect to="/register"/>
                                )}/>
                                <Route path="/user/profile/:id" render={() => (isAuthenticated ?
                                        <Profile/> : <Redirect to="/login"/>
                                )}/>
                                <Route path="/category/:category" exact userId={user.id} component={AllContent}/>
                                <Route path="/category/:category/:content" userId={user.id} component={Content}/>
                                <Route path="/administration/manage-roles"
                                       render={() => (userRoles('ROLE_ADMIN' || 'ROLE_MODERATOR') ?
                                               <ManageRole/> : <Redirect to="/"/>
                                       )}/>
                                <Route path="/administration/create-content"
                                       render={() => (userRoles('ROLE_MODERATOR' || 'ROLE_ADMIN' || 'ROLE_ROOT_ADMIN') ?
                                               <CreateContent/> : <Redirect to="/"/>
                                       )}/>
                                <Route path="/create-word" component={CreateWord}/>
                                <Route component={NotFound}/>
                            </Switch>
                        </UserContext.Provider>
                    </div>
                )
                }
            </BrowserRouter>
        </Fragment>
    )
}

export default App