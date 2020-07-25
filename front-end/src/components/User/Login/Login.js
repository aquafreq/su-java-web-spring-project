import React, {useState} from 'react'

import {FormControl} from '@material-ui/core'
import {MDBAlert} from "mdbreact"
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import styles from './Login.module.css'
import RegisterLoginNav from "../../Navigation/RegisterLoginNav"
import {useHistory} from "react-router-dom"

const Login = ({login}) => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const history = useHistory()

    return (
        <>
            <Navigation/>
            <div className={styles.container}>

                {error && <MDBAlert className={styles.error} color="danger"> {error}</MDBAlert>}
                <form action="/login" method="post">
                    <FormControl>
                        <h2>Login</h2>
                        <label
                            htmlFor="username">
                            <input type="text"
                                   id="username"
                                   aria-describedby="my-helper-text"
                                   name="username"
                                   autoFocus
                                   placeholder="Username..."
                                   value={username} onChange={(ev) => setUsername(ev.target.value)}
                            />
                        </label>
                        <label
                            htmlFor="password">
                            <input type="password"
                                   id="username"
                                   aria-describedby="my-helper-text"
                                   placeholder="Password..."
                                   name="password"
                                   value={password} onChange={(ev) => setPassword(ev.target.value)}
                            />
                        </label>
                        <button onClick={async (e) => {
                            e.preventDefault()
                            const error = await login(username, password)
                            if (error) {
                                setError(error.response.headers.error || 'Incorrect username or password!')
                                setUsername('')
                                setPassword('')
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