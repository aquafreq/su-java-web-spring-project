import React, {useEffect, useState} from 'react'

import {Link, useHistory} from 'react-router-dom'
import styles from '../UserDetails/UserDetails.module.css'
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import userService from "../../../services/UserService"

export default function (props) {
    const [user, setUser] = useState({})
    const history = useHistory()

    useEffect(() => {
        fetchUser()
    }, [])

    const fetchUser = async () => {
        const {pathname} = history.location
        const response = await userService.userDetails(pathname)
        const userData = await response.data
        userData.hobbies = userData.hobbies.join(', ')
        setUser(userData)
    }

    function renderUser() {
        return (
            <section>
                <fieldset>
                    <p>Username: {user.username}</p>
                    <p>Email: {user.email}</p>
                    <p>Is enabled: {user.isEnabled}</p>
                    <p>Authorities: {user.authorities}</p>
                    <p>Registered on: {user.registrationDate}</p>
                    <p>Born: {user.birthDate}</p>
                    <p>Hobbies: {user.hobbies}</p>
                    <p>Nationality: {user.nationality}</p>
                    <p>Activity: {user.userProfileActivity}</p>
                    <p>Level of experience: {user.levelExperience}</p>
                    <p>Level of language: {user.levelOfLanguage}</p>
                </fieldset>
            </section>
        )
    }

    return (
        <div>
            <Navigation/>
            <div className={styles.wrapper}>
                <div className={styles['user-details']}>
                    <h3>User details: </h3>
                    {renderUser()}
                </div>
                <Link to='/administration/manage-users'>
                    Go to user management
                </Link>
            </div>
            <Footer/>
        </div>
    )
}
