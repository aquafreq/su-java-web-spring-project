import React, {useState} from 'react'

import {FormControl} from '@material-ui/core'
import {MDBAlert} from "mdbreact"
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import styles from './Login.module.css'
import errorStyles from '../../ErrorStyles.module.css'
import RegisterLoginNav from "../../Navigation/RegisterLoginNav"
import {useHistory} from "react-router-dom"

const Login = ({login}) => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [errorTrigger, setErrorTrigger] = useState(false)
    const history = useHistory()

    const errors = {
        usernameError: username.length > 2 ? '' : 'Username must be more than 2 characters!',
        passwordError: password.length > 2 ? '' : 'Password must be more than 2 characters!',
    }

    return (
        <>
            <Navigation/>
            <div className={styles.container}>

                {error && <MDBAlert className={styles.error} color="danger"> {error}</MDBAlert>}
                <form action="/login" method="post">
                    <FormControl>
                        <h2>Login</h2>
                        {errorTrigger && errors.usernameError &&
                        <h5 className={errorStyles.error}>{errors.usernameError}</h5>}
                        <label
                            htmlFor="username">
                            <input type="text"
                                   autoComplete="off"
                                   style={(errorTrigger && errors.usernameError) ? {border: '4px solid red'} : null}
                                   id="username"
                                   aria-describedby="my-helper-text"
                                   name="username"
                                   autoFocus
                                   placeholder="Username..."
                                   value={username} onChange={(ev) => setUsername(ev.target.value)}
                            />
                        </label>
                        {errorTrigger && errors.passwordError &&
                        <h5 className={errorStyles.error}>{errors.passwordError}</h5>}
                        <label
                            htmlFor="password">
                            <input type="password"
                                   autoComplete="off"
                                   style={(errorTrigger && errors.usernameError) ? {border: '4px solid red'} : null}
                                   id="username"
                                   aria-describedby="my-helper-text"
                                   placeholder="Password..."
                                   name="password"
                                   value={password} onChange={(ev) => setPassword(ev.target.value)}
                            />
                        </label>
                        <button onClick={async (e) => {
                            e.preventDefault()
                            setErrorTrigger(true)
                            if (!errors.passwordError && !errors.usernameError) {
                                const error = await login(username, password)
                                if (error) {
                                    setError(error)
                                    setErrorTrigger(false)
                                    setUsername('')
                                    setPassword('')
                                }
                            }
                        }
                        }>Login
                        </button>
                    </FormControl>
                </form>
                <RegisterLoginNav path={history.location.pathname}/>
            </div>
            <Footer/>
        </>
    )
}
export default Login