import React, {Fragment, createContext, useCallback, useContext, useEffect, useLayoutEffect, useState} from 'react'

import {BrowserRouter, Redirect, Route, Switch, useHistory} from 'react-router-dom'

import styles from './App.module.css'
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
import ManageUsers from "./components/AdminPanel/ManageRole"
import UserContext from "./auth/UserContext"
import Profile from "./components/User/UserProfile/UserProfile"
import AllContent from "./components/AdminPanel/Content/AllContent";

import Content from "./components/AdminPanel/Content/Content";
import UserProfileEdit from "./components/User/UserProfile/UserProfileEdit";
import UserDetails from "./components/User/UserDetails/UserDetails";
import PracticeWords from "./components/User/UserProfile/PracticeWords";
import UserChangePassword from "./components/User/UserChange/UserChangePassword";

const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("authorization"))
    const [isLoading, setIsLoading] = useState(true)
    const [user, setUser] = useState({username: '', id: '', authorities: []})
    const history = useHistory()

    useLayoutEffect(() => {
        setUserState();
    }, [isAuthenticated])

    const setUserProps = (user) => {
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

    const userRolesIncludes = (...auth) => {
        const userRoles = localStorage.getItem('auth').split('; ')


        return auth.some(role => userRoles.includes(role))
    }

    const logout = () => {
        localStorage.clear()
        setIsAuthenticated(false)
        history.push('/')
    }

    function setUserState() {
        let token = localStorage.getItem("authorization");
        (async () => {
            try {
                let axiosResponse = await userService.getCurrentUser(token)
                localStorage.setItem('userId', axiosResponse.data.id)
                localStorage.setItem('auth',
                    axiosResponse
                        .data
                        .authorities
                        .map(x => x.authority).join('; '))

                setUserProps(axiosResponse.data)
                setIsLoading(false)
            } catch (e) {
                setUserProps({id: '', username: '', authorities: []})
                setIsLoading(false)
            }
        })()
    }

    async function updateUser(url, user, id) {
        const response = await userService.updateUser(url, user, id)
        const editedUser = await response.data
        setUser(prev => ({...prev, username: editedUser.username}))
        setTimeout(() => history.push(`/user/profile/${id}`),300)
    }

    // debugger
    return (
        <Fragment>
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
                            <Route exact path="/user/profile/:id" render={() => (isAuthenticated ?
                                    <Profile/> : <Redirect to="/login"/>
                            )}/>
                            <Route path="/user/profile/:id/practice" component={PracticeWords}/>
                            <Route path="/user/profile/:id/edit" render={props => <UserProfileEdit {...props} updateUser={updateUser} />}/>
                            <Route path="/user/profile/:id/change-password"
                                   render={() => isAuthenticated ? <UserChangePassword /> : <Redirect to="/" />}/>
                            <Route path="/user/details/:id" component={UserDetails}/>
                            <Route path="/category/:category" exact userId={user.id} component={AllContent}/>
                            <Route path="/category/:category/:content" userId={user.id} component={Content}/>
                            <Route path="/administration/manage-users"
                                   render={() => (userRolesIncludes('ROLE_ADMIN', 'ROLE_ROOT_ADMIN') ?
                                           <ManageUsers/> : <Redirect to="/"/>
                                   )}/>
                            <Route path="/administration/create-content"
                                   render={() =>
                                       userRolesIncludes('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_ROOT_ADMIN') ?
                                           <CreateContent/> : <Redirect to="/"/>
                                   }/>
                            <Route path="/administration/user-logs"/>
                            <Route path="/create-word" component={CreateWord}/>
                            <Route path="*" component={NotFound}/>
                        </Switch>
                    </UserContext.Provider>
                </div>
            )
            }
        </Fragment>
    )
}

export default App