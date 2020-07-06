import React, {useState} from 'react';
import {Route, Switch, BrowserRouter, NavLink, Link} from 'react-router-dom';
import Navigation from './components/Navigation/Navigation'
import Footer from './components/Footer/Footer'
import Home from './components/Home/Home'
import Login from './components/Login/Login'
import Register from './components/Register/Register'
import {Loading} from "./components/Loading/Loading";
import userService from "../src/services/UserService"

const App = (props) => {
    const [isLogged, setIsLogged] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [user, setUser] = useState({username: null, id: null, authentication: []});


    const login = (username, password) => {
        debugger

        userService.login(username, password)
            .then(r => {
                {
                    console.log(r);
                    setIsLogged(true);

                    setUser(
                        r.data.username
                        // id: r.data.id,
                        // authentication: r.data.authorities
                    );
                }
                debugger
            });
    }

    const logout = () => {
        let promise = userService.logout();
        promise.then(r => {
            console.log(r);
            // setUser({username: null, id, null});
            debugger
        })
        props.history.push('/');
        // localStorage.clear();
    }

    const register = ({username, password, email}) => {
        debugger
        userService.register(username, password, email)
            .then(r => {

                console.log(r);
                debugger
            });

        props.history.push('/login');
        // localStorage.clear();
    }
    // if (isLoading) {return <Loading />}

    return (
        <div className="App">
            <Navigation isLogged={isLogged} logout={logout}/>
            <Switch>
                <Route path="/" exact component={Home}/>
                <Route path="/login" login={login} component={(props) => <Login login={login} {...props} />}/>
                <Route path="/register" component={Register}/>
            </Switch>
            <Footer/>
        </div>
    );
}

export default App;


// <Router>
//     <Switch>
//         {/* <Route path='/api/upper-side' component={UpperSide}/>
//         <Route path='/api/dogs' exact component={AllDogs}/>
//         <Route path='/api/dog/:id' exact component={Dog}/> */}
//     </Switch>
// </Router>