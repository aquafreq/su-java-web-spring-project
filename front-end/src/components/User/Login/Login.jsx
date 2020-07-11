import React, {useState} from 'react';

import {FormControl, InputLabel, Input, FormGroup, Button, FilledInput} from '@material-ui/core';
import {MDBAlert} from "mdbreact";

const Login = ({login}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    return (
        <>
            {error && <MDBAlert color="danger"> {error}</MDBAlert>}
            <form action="/login" method="post">
                <FormControl>
                    <h5>Login</h5>
                    <FormControl>
                        <InputLabel htmlFor="username">Username</InputLabel>
                        <Input id="username" aria-describedby="my-helper-text" name="username"
                               value={username} onChange={(ev) => setUsername(ev.target.value)}
                        />
                    </FormControl>
                    <FormControl>
                        <InputLabel htmlFor="password">Password</InputLabel>
                        <Input id="register" aria-describedby="my-helper-text" name="password"
                               value={password} onChange={(ev) => setPassword(ev.target.value)}
                        />
                    </FormControl>
                    <Button onClick={async (e) => {
                        e.preventDefault();
                        if (await login(username, password)) {
                            setError('Incorrect username or password')
                            setUsername('')
                            setPassword('')
                        }
                    }
                    }>Login</Button>
                </FormControl>
            </form>
        </>
    )
}
export default Login;