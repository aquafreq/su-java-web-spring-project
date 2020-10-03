import React, {useEffect, useState} from 'react'
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
    const [errors, setErrors] = useState({
        username: '',
        password: '',
        email: '',
    })

    const [isTouched, setIsTouched] = useState(false)
    const history = useHistory()

    // const errors = {
    //     usernameError: username.length > 2 ? '' : 'Username must be more than 2 characters!',
    //     passwordError: password.length > 2 ? '' : 'Password must be more than 2 characters!',
    //     emailError: email.length > 3 && email.includes('@') ? '' : 'Email must be valid and above 3 characters!',
    // }

    return (
        <>
            <Navigation/>
            <div className={styles.container}>
                <div>
                    {isTouched && error &&
                    <MDBAlert className={errorStyles['mdb-alert']}
                              color="danger"> {error}</MDBAlert>}
                    <form>
                        <h1>Register</h1>
                        {isTouched && errors.username &&
                        <MDBAlert className={errorStyles['mdb-alert']}
                                  color="danger"> {errors.username.join(' && ')}</MDBAlert>}
                        <div>
                            <input value={username} onChange={(ev) => setUsername(ev.target.value)}
                                   autoFocus
                                   type="text"
                                   placeholder="Username..."/>
                        </div>
                        {isTouched && errors.password &&
                        <MDBAlert className={errorStyles['mdb-alert']}
                                  color="danger"> {errors.password}</MDBAlert>}
                        <div>
                            <input value={password} onChange={(ev) => setPassword(ev.target.value)}
                                   type="password"
                                   placeholder="Password..."/>
                        </div>
                        {isTouched && errors.email &&
                        <MDBAlert className={errorStyles['mdb-alert']} color="danger"> {errors.email}</MDBAlert>}
                        <div>
                            <input value={email} onChange={(ev) => setEmail(ev.target.value)}
                                   type="email" placeholder="Email..."/>
                        </div>
                        <MDBBtn onClick={e => {
                            e.preventDefault()

                            register(username, password, email)
                                .then(e => {
                                    if (e.data.message) {
                                        setError(e.data.message)
                                        setErrors({})
                                        setIsTouched(true)
                                    } else {
                                        history.push('/login')
                                    }
                                }, e => {
                                    setError('')
                                    setErrors(e.response.data)
                                    setIsTouched(true)
                                })
                        }
                        } gradient="purple">Register
                        </MDBBtn>
                    </form>
                    <RegisterLoginNav path={history.location.pathname}/>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default Register