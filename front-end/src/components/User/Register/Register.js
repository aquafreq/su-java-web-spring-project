import React, {useState} from 'react'
import {MDBAlert, MDBBtn} from "mdbreact"
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
    const history = useHistory()


    //validatii TODO
    return (
        <>
        <Navigation/>
        <div  className={styles.container}>
            <div>
                <form>
                    <h1>Register</h1>
                    {error && <MDBAlert color="danger"> {error}</MDBAlert>}
                    <div>
                        <input value={username} onChange={(ev) => setUsername(ev.target.value)}
                               autoFocus
                               type="text"
                               placeholder="Username..."/>
                    </div>
                    <div>
                        <input value={password} onChange={(ev) => setPassword(ev.target.value)}
                               type="password"
                               placeholder="Password..."/>
                    </div>
                    <div>
                        <input value={email} onChange={(ev) => setEmail(ev.target.value)}
                               type="email" placeholder="Email..."/>
                    </div>

                    <MDBBtn onClick={async (e) => {
                        e.preventDefault()

                        const error = await register(username, password, email)

                        if (error) {
                            setError(error.response.data)
                            setUsername('')
                            setPassword('')
                            setEmail('')
                        }
                    }
                    } gradient="purple">Register</MDBBtn>
                </form>
                <RegisterLoginNav path={history.location.pathname} />
            </div>
        </div>
    <Footer/>
    </>
    )
}

export default Register