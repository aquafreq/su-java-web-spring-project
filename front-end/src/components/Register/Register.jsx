import React, {useState} from 'react';
import {MDBBtn} from "mdbreact";

import userService from "../../services/UserService";

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');

    const registerHandler = () => {
        userService.register(username,
            password,
            email);
    }

    return (
        <div>
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
            <MDBBtn onClick={registerHandler} gradient="purple">Purple</MDBBtn>
            <MDBBtn onClick={registerHandler} gradient="peach">Peach</MDBBtn>
            <MDBBtn onClick={registerHandler} gradient="aqua">Aqua</MDBBtn>
        </div>
    );
}

export default Register;