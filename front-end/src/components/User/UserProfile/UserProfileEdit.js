import React, {Fragment, useContext, useEffect, useState} from "react"

import {useHistory} from 'react-router-dom'
import styles from './UserProfileEdit.module.css'
import userService from "../../../services/UserService"
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import InputLabel from "../../index"
import UserContext from "../../../auth/UserContext";

export default function ({updateUser}) {
    const experience = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED',]
    const levels = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2',]
    const userContext = useContext(UserContext)

    const [user, setUser] = useState({
        username: '',
        email: '',
        birthDate: '',
        nationality: '',
        hobbies: '',
        levelOfLanguage: '',
        levelExperience: '',
    })

    const [errors, setErrors] = useState({
        username: false,
        email: false,
    })

    const history = useHistory()
    const url = history.location.pathname

    useEffect(() => {
        fetchData()
    }, [])

    function fetchData() {
        const userProfileUrl = url.substring(0, url.lastIndexOf('/'))
        userService
            .userProfile(userProfileUrl)
            .then(user => {
                setUser(user.data)
            })
    }

    function handleSubmit(e) {
        e.preventDefault()
        if (!(errors.email || errors.username)) {

            const editUser = {...user}
            if (editUser.hobbies && editUser.hobbies.includes(',')) {
                editUser.hobbies = editUser.hobbies.split(', ')
            }

            updateUser(url, editUser, userContext.id)
        }
    }

    const validation = {
        username: (val) => {
            const error = !(val.length > 2)
            setErrors(prevState => ({
                ...prevState,
                username: error
            }))
        },
        email: (val) => {
            const error = !(val.length > 4 && val.includes('@'))
            setErrors(prevState => ({
                ...prevState,
                email: error
            }))
        },
    }

    function handleChange(e) {
        e.preventDefault()
        const {name, value} = e.target

        setUser(prevState => ({
            ...prevState,
            [name]: value
        }))
    }

    function onBlur(e) {
        const {name, value} = e.target
        validation[name](value)
    }

    function renderOptions(array) {
        return array.map((e, i) => <option key={i} value={e || ''}>{e}</option>)
    }

    return (
        <Fragment>
            <Navigation/>
            <div className={styles.container}>

                <form onSubmit={handleSubmit}>
                    <fieldset>
                        <legend>Edit profile</legend>
                        {errors.username &&
                        <div className={styles.error}>Username must be more than 2 characters !</div>}
                        <InputLabel labelName='Username'
                                    value={user.username || ''}
                                    onChange={handleChange}
                                    name='username'
                                    required
                                    min='3'
                                    onBlur={onBlur}
                        />
                        {errors.email &&
                        <div className={styles.error}>Email must be more than 3 characters and must contain @ !</div>}
                        <InputLabel labelName='Email'
                                    value={user.email || ''}
                                    onChange={handleChange}
                                    name='email'
                                    type="email"
                                    required
                                    onBlur={onBlur}
                        />
                        <InputLabel labelName={'Birth date'}
                                    value={user.birthDate || ''}
                                    onChange={e => {
                                        const date = e.target.value
                                        setUser(prevState => ({
                                            ...prevState, birthDate: date
                                        }))

                                    }}
                                    type="date"
                        />
                        <InputLabel labelName={'Nationality'}
                                    onChange={handleChange}
                                    name='nationality'
                                    value={user.nationality || ''}
                        />
                        <InputLabel labelName={'Hobbies'}
                                    onChange={handleChange}
                                    value={user.hobbies || ''}
                                    name='hobbies'
                                    placeholder="Books, games, sport..."
                        />
                        <div>
                            <label>
                                Language Level:
                                <select
                                    onChange={e => {
                                        const levelOfLanguage = e.target.value
                                        setUser(prevState => ({...prevState, levelOfLanguage}))
                                    }}
                                >
                                    <option defaultValue={user.levelOfLanguage}>{user.levelOfLanguage}</option>
                                    {renderOptions(levels)}
                                </select>
                            </label>
                        </div>
                        <div>
                            <label>
                                Experience:
                                <select onChange={e => {
                                    const levelExperience = e.target.value
                                    setUser(prevState => ({
                                        ...prevState,
                                        levelExperience
                                    }))
                                }
                                }>
                                    <option defaultValue={user.levelExperience}>{user.levelExperience}</option>
                                    {renderOptions(experience)}
                                </select>
                            </label>
                        </div>
                        <input value="Edit" type="submit"/>
                    </fieldset>
                </form>
            </div>
            <Footer/>
        </Fragment>
    )
}