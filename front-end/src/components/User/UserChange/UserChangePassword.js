import React, {Fragment, useEffect, useState} from "react";

import styles from './UserChangePassword.module.css'
import Navigation from "../../Navigation/Navigation";
import Footer from "../../Footer/Footer";
import Input from "../../index"
import userService from "../../../services/UserService";
import {useHistory} from 'react-router-dom'

export default function () {
    const [current, setCurrent] = useState('')
    const [password, setPassword] = useState('')
    const [rePassword, setRePassword] = useState('')
    const [error, setError] = useState('')
    const history = useHistory()

    function handleSubmit(e) {
        e.preventDefault()
        const url = history.location.pathname
        if (current.trim().length &&
            password.trim().length !== 0 &&
            password === rePassword) {
            userService
                .savePassword(url, {oldPassword: current, newPassword: password})
                .then((resp) => {
                    debugger
                    if (resp.data) {
                        history.push(url.substring(0, url.lastIndexOf('/') + 1))
                    }
                    setError('Invalid user password is given!')
                })
        } else {
            setError('Wrong input!')
        }
    }


    return (
        <Fragment>
            <Navigation/>
            <div className={styles.container}>
                <h2>Change passwords</h2>
                <h5>Make sure it's secure enough!</h5>
                <form onSubmit={handleSubmit}>
                    <Input
                        labelName='Old password: '
                        value={current}
                        required
                        onChange={e => setCurrent(e.target.value)}
                    />
                    <Input type='password'
                           labelName='New password: '
                           required
                           value={password}
                           onChange={e => setPassword(e.target.value)}
                    />
                    <Input type='password'
                           labelName='Repeat new password: '
                           required
                           value={rePassword}
                           onChange={e => setRePassword(e.target.value)}
                    />
                    {error && <div className={styles.error}>{error}</div>}
                    <Input type='submit'/>
                </form>
            </div>
            <Footer/>
        </Fragment>
    )
}