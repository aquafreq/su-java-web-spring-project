import React, {useState} from 'react'
import {MDBAlert, MDBBtn} from "mdbreact"
import errorStyles from '../../ErrorStyles.module.css'
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import {useHistory} from "react-router-dom"

import styles from './Register.module.css'
import RegisterLoginNav from "../../Navigation/RegisterLoginNav"

const Register = ({register}) => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [email, setEmail] = useState('')
    const [error, setError] = useState('')
    const [isTouched, setIsTouched] = useState(false)
    const history = useHistory()

    const errors = {
        usernameError: username.length > 2 ? '' : 'Username must be more than 2 characters!',
        passwordError: password.length > 2 ? '' : 'Password must be more than 2 characters!',
        emailError: email.length > 3 && email.includes('@') ? '' : 'Email must be valid and above 3 characters!',
    }

    return (
        <>
            <Navigation/>
            <div className={styles.container}>
                <div>
                    <form>
                        <h1>Register</h1>
                        {isTouched && errors.usernameError &&
                        <MDBAlert className={errorStyles['mdb-alert']} color="danger"> {errors.usernameError}</MDBAlert>}
                        <div>
                            <input value={username} onChange={(ev) => setUsername(ev.target.value)}
                                   autoFocus
                                   type="text"
                                   placeholder="Username..."/>
                        </div>
                        {isTouched && errors.passwordError &&
                        <MDBAlert className={errorStyles['mdb-alert']} color="danger"> {errors.passwordError}</MDBAlert>}
                        <div>
                            <input value={password} onChange={(ev) => setPassword(ev.target.value)}
                                   type="password"
                                   placeholder="Password..."/>
                        </div>
                        {isTouched && errors.emailError && <MDBAlert className={errorStyles['mdb-alert']} color="danger"> {errors.emailError}</MDBAlert>}
                        <div>
                            <input value={email} onChange={(ev) => setEmail(ev.target.value)}
                                   type="email" placeholder="Email..."/>
                        </div>
                        <MDBBtn onClick={async (e) => {
                            e.preventDefault()
                            setIsTouched(true)
                            if (!errors.usernameError && !errors.passwordError && !errors.emailError) {
                                const error = await register(username, password, email)
                                debugger
                                if (error) {
                                    setError(error.response.data)
                                    setUsername('')
                                    setPassword('')
                                    setEmail('')
                                }
                            }
                        }
                        } gradient="purple">Register</MDBBtn>
                    </form>
                    <RegisterLoginNav path={history.location.pathname}/>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default Register