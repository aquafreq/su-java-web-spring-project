import React, {useState} from 'react';
import {FormControl, InputLabel, Input, FormGroup, Button, FilledInput} from '@material-ui/core';

const Login = ({login, props}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    // function handleSubmit() {
    //     return () => {
    //         userService.login(username, password);
    //     };
    // }

    return (
        <>
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
                    <Button onClick={() => login(username, password)}>Login</Button>
                </FormControl>
            </form>
        </>
    )
}
export default Login;