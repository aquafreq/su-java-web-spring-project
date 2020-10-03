import React, {Fragment, useLayoutEffect, useState} from 'react'

import {Redirect, Route, Switch, useHistory} from 'react-router-dom'

import styles from './App.module.css'
import Home from './components/Home/Home'
import Login from './components/User/Login/Login'
import Register from './components/User/Register/Register'
import Logout from './components/User/Logout/Logout'
import userService from "../src/services/UserService"
import NotFound from "./components/NotFound/NotFound"
import {Loading} from "./components/Loading/Loading"
import CreateContent from "./components/AdminPanel/CreateContent/CreateContent"
import ManageUsers from "./components/AdminPanel/ManageRole"
import UserContext from "./auth/UserContext"
import Profile from "./components/User/UserProfile/UserProfile"
import AllContent from "./components/AdminPanel/Content/AllContent";

import Content from "./components/AdminPanel/Content/Content";
import UserProfileEdit from "./components/User/UserProfile/UserProfileEdit";
import UserDetails from "./components/User/UserDetails/UserDetails";
import UserChangePassword from "./components/User/UserChange/UserChangePassword";
import Practice from "./components/User/Practice/Practice";
import AllLogs from "./components/User/Logs/AllLogs";
import UserLogs from "./components/User/Logs/UserLogs";

const ADMIN = 'ROLE_ADMIN';
const ROOT_ADMIN = 'ROLE_ROOT_ADMIN';
const MODERATOR = 'ROLE_MODERATOR';
const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("authorization"))
    const [isLoading, setIsLoading] = useState(true)
    const [user, setUser] = useState({username: '', id: '', authorities: []})
    const history = useHistory()

    useLayoutEffect(() => {
        setUserState()
    }, [isAuthenticated])

    const setUserProps = (user) => {
        setUser({...user})
        setIsLoading(false)
    }

    function setUserState() {
        let token = localStorage.getItem("authorization");

        debugger
        if (token) {
            (async () => {
                try {
                    let axiosResponse = await userService.getCurrentUser(token)
                    const userData = await axiosResponse.data

                    setUserProps(userData)
                    localStorage.setItem('userId', userData.id)
                    localStorage.setItem('auth',
                        userData
                            .authorities
                            .map(x => x.authority).join('; '))
                    debugger
                    setIsLoading(false)
                } catch (e) {
                    localStorage.clear()
                    setIsAuthenticated(false)
                    setUserProps({id: '', username: '', authorities: []})
                }
            })()
        } else {
            localStorage.clear()
            setIsAuthenticated(false)
            setUserProps({id: '', username: '', authorities: []})
        }
    }

    const login = async (username, password) => {
        return await userService.login(username, password)
            .then((r) => {
                setIsLoading(true)
                localStorage.setItem("authorization", r.headers.authorization)
                setIsAuthenticated(true)
                history.push('/')
            }, e => {
                return e.response.status === 401 ? 'Invalid credentials!' : e.response.data.message
            })
    }

    const register = async (username, password, email) => {
        return await userService
            .register(username, password, email)
    }

    const logout = () => {
        localStorage.clear()
        document.cookie = ''
        setIsAuthenticated(false)
        // history.push('/')
    }

    function updateUser(url, user) {
        userService.updateUser(url, user)
            .then((u) => {
                debugger
                debugger
                userService.getCurrentUser(localStorage.getItem('authorization'))
                    .then(u => setUser(u.data))
                    .then(() =>
                        setTimeout(() => history.push(url.substring(0, url.lastIndexOf('/'))), 500))
            })
    }

    const userRolesIncludes = (...auth) => {
        if (localStorage.getItem('auth')) {
            const userRoles = localStorage.getItem('auth').split('; ')
            return auth.some(role => userRoles.includes(role))
        }

        return false
    }

    return (
        <Fragment>
            <div className={styles.app}>
                {isLoading ? <Loading/> : (
                    <UserContext.Provider
                        value={{
                            username: user.username,
                            id: user.id,
                            // userRoles:[]
                            userRoles: user.authorities.map(r => r.authority)
                        }}>
                        <Switch>
                            <Route path="/" exact isAuthenticated={isAuthenticated} component={Home}/>
                            <Route path="/login" exact render={() => (!isAuthenticated ?
                                <Login login={login}/> : <Redirect to="/"/>)}/>
                            <Route path="/register" render={() => (!isAuthenticated ?
                                <Register register={register}/> : <Redirect to="/"/>)}/>
                            <Route path="/logout" render={() => (!!localStorage.getItem("auth") ?
                                    <Logout logout={logout}/> : <Redirect to="/register"/>
                            )}/>
                            <Route path="/user/profile/:id/practice" component={Practice}/>
                            <Route exact path="/user/profile/:id" render={() => (isAuthenticated ?
                                    <Profile/> : <Redirect to="/login"/>
                            )}/>
                            <Route path="/user/profile/:id/edit"
                                   render={props => <UserProfileEdit {...props} updateUser={updateUser}/>}/>
                            <Route path="/user/profile/:id/change-password"
                                   render={() => isAuthenticated ? <UserChangePassword/> : <Redirect to="/"/>}/>
                            <Route path="/user/details/:id"
                                   render={
                                       (props) =>
                                           userRolesIncludes(ROOT_ADMIN, ADMIN) ?
                                               <UserDetails {...props} /> : <Redirect to='/'/>
                                   }/>
                            <Route path="/category/:category" exact component={AllContent}/>
                            <Route path="/category/:category/:content" userId={user.id} component={Content}/>
                            <Route path="/administration/manage-users"
                                   render={() => (userRolesIncludes(ADMIN, ROOT_ADMIN) ?
                                           <ManageUsers/> : <Redirect to="/"/>
                                   )}/>
                            <Route path="/administration/create-content"
                                   render={() =>
                                       userRolesIncludes(MODERATOR, ADMIN, ROOT_ADMIN) ?
                                           <CreateContent/> : <Redirect to="/"/>
                                   }/>
                            <Route exact path="/administration/logs"
                                   render={() =>
                                       userRolesIncludes(ADMIN, ROOT_ADMIN) ?
                                           <AllLogs/> : <Redirect to="/"/>}
                            />
                            <Route path="/administration/logs/:username"
                                   render={() =>
                                       userRolesIncludes(ADMIN, ROOT_ADMIN) ?
                                           <UserLogs/> : <Redirect to="/"/>}
                            />
                            <Route component={NotFound}/>
                        </Switch>
                    </UserContext.Provider>
                )
                }
            </div>
        </Fragment>
    )
}

export default App