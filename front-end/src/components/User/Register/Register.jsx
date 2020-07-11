import React, {useState} from 'react';
import {MDBAlert, MDBBtn} from "mdbreact";

const Register = ({register}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    return (
        <div>
            <h1>Register</h1>
            {error && <MDBAlert color="danger"> {error}</MDBAlert>}
            <form action="/register">
                <div>
                    <label>Username:</label>
                    <input value={username} onChange={(ev) => setUsername(ev.target.value)}
                           type="text"
                           placeholder="Username"/>
                </div>
                <div>
                    <label>Password</label>
                    <input value={password} onChange={(ev) => setPassword(ev.target.value)}
                           type="password"
                           placeholder="Password"/>
                </div>
                <div>
                    <label>Email</label>
                    <input value={email} onChange={(ev) => setEmail(ev.target.value)}
                           type="email" placeholder="Email"/>
                </div>
            </form>
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
            } gradient="purple">Purple</MDBBtn>
        </div>
    );
}

export default Register;