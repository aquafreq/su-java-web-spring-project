import React, {useContext, useEffect, useRef, useState} from 'react'

import {Link, useHistory} from 'react-router-dom'
import styles from '../UserDetails/UserDetails.module.css'
import Navigation from "../../Navigation/Navigation";
import Footer from "../../Footer/Footer";
import userService from "../../../services/UserService";

export default function () {
    const [user, setUser] = useState({})
    const history = useHistory()

    useEffect(async () => {
        fetchUser()
    }, [])

    const fetchUser = async () => {
        const { state } = history.location
        debugger
        const response = await userService.userProfile(state)
        const userData = await response.data
        debugger
        setUser(userData)
    }

    return (
        <div>
            <Navigation/>
            <div className={styles.wrapper}>
                <div className={styles['user-details']}>
                    <h3>User details: </h3>
                    <section>
                        <fieldset>Username: {user.username}</fieldset>
                        <br/>
                        <fieldset>Activity: {user.activity}</fieldset>
                        <br/>
                        <fieldset>Born: {user.birthDate}</fieldset>
                        <br/>
                        <fieldset>Nationality: {user.nationality}</fieldset>
                        <br/>
                        <fieldset>Hobbies: {user.hobbies}</fieldset>
                        <br/>
                    </section>
                </div>
                <Link to='/administration/manage-users'>
                    Go back to admin page
                </Link>
            </div>
            <Footer/>
        </div>
    )
}
