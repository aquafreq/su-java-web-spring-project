import React, {useContext, useEffect, useRef, useState} from "react"

import UserContext from "../../../auth/UserContext";
import userService from "../../../services/UserService";
import Navigation from "../../Navigation/Navigation";
import Footer from "../../Footer/Footer";
import {Link, useHistory} from 'react-router-dom'

import styles from './UserProfile.module.css'

const errorStyles = {color: 'red', fontSize: '26px', fontWeight: 700}

export default function () {
    const userContext = useContext(UserContext)

    const [wordsCategory, setWordsCategory] = useState([])
    const [user, setUser] = useState({})
    const [category, setCategory] = useState('')
    const [chosenCategoryId, setChosenCategoryId] = useState('')
    const [text, setText] = useState('Hide profile')
    const [word, setWord] = useState({name: '', definition: ''})
    const [categoryExists, setCategoryExists] = useState('')
    const [wordExists, setWordExists] = useState('')
    const [categoryError, setCategoryError] = useState([])
    const [wordError, setWordError] = useState({name:'',definition:''})

    const profile = useRef('')
    const info = useRef()

    const history = useHistory()

    useEffect(() => {
        info.current.style.display = 'none'
        const url = history.location.pathname
        const userId = userContext.id || url.substring(url.lastIndexOf('/') + 1)

        Promise.all([
            userService.userProfile(url),
            userService.fetchUserCategories(userId)
        ]).then(([user, categories]) => {
            setUser(user.data)
            setWordsCategory(categories.data)
        })
    }, [])

    function hideProfile() {
        profile.current.style.visibility = !profile.current.style.visibility ? 'hidden' : ''
        profile.current.style.visibility ? setText('Show profile') : setText('Hide profile')
    }

    function handleSubmitWord(e) {
        e.preventDefault()
        userService
            .createWordForCategory(userContext.id, chosenCategoryId, word)
            .then(e => {
                if (e.data.message) {
                    setWordExists(e.data.message)
                } else {
                    setWordExists('')
                    setWord({name: '', definition: ''})
                }
                setWordError({})
            }, e => {
                setWordExists('')
                setWordError(e.response.data)
            })
    }

    async function handleSubmitCategory(e) {

        e.preventDefault()
        let response
        try {
            response = await userService.createCategoryForUser(userContext.id, {name: category})
            let responseData = await response.data

            if (responseData.message) {
                setCategoryExists(responseData.message)
                return
            }

            setCategoryExists('')
            setCategory('')
            setCategoryError([])
            setWordsCategory([...wordsCategory, responseData])
        } catch (e) {
            setCategoryError(e.response.data.name)
        }
    }

    function renderCategories() {
        return (
            <select required onChange={e => setChosenCategoryId(e.target.value)}>
                <option value=''>Choose category</option>
                {wordsCategory
                    .map(x => <option key={x.id} value={x.id}>{x.name}</option>
                    )
                }
            </select>
        )
    }

    return (
        <div>
            <Navigation/>
            <div className={styles['profile-container']}>
                <div className={styles['profile-wrapper']}>
                    <button onClick={hideProfile} className={styles.close}>{text}
                    </button>
                    <div ref={profile} className={styles.profile}>
                        <h3>Profile details: </h3>
                        <aside>
                            <fieldset>Username: {user.username}</fieldset>
                            <fieldset>Email: {user.email}</fieldset>
                            <fieldset>Birth date: {user.birthDate}</fieldset>
                            <fieldset>Nationality: {user.nationality}</fieldset>
                            <fieldset>Hobbies: {Array.isArray(user.hobbies) ? user.hobbies.join(', ') : user.hobbies}</fieldset>
                            <fieldset>Language level: {user.levelOfLanguage}</fieldset>
                            <fieldset>Experience: {user.levelExperience}</fieldset>
                            <br/>
                            <Link to={`/user/profile/${userContext.id}/edit`}>
                                Edit profile
                            </Link>
                            <br/>
                            <Link to={`/user/profile/${userContext.id}/change-password`}>
                                Change password
                            </Link>
                        </aside>
                    </div>
                </div>
                <div className={styles.grid}>
                    <div className={styles.middle}>
                        {categoryExists && <h2 style={{
                            color: 'red',
                            fontWeight: 700,
                        }}>{categoryExists}</h2>}
                        <h3>Create categories and add new unknown words and the definitions you wanna learn for
                            them!</h3>
                        {!!categoryError.length &&
                        categoryError.map(e =>
                            <span style={errorStyles}>{e}<br/></span>
                        )}
                        <form>
                            <label>
                                Add a category and get started with learning!
                                <br/>
                                <input type="text" value={category} onChange={e => setCategory(e.target.value)}/>
                            </label>
                            <input value="Add" type="submit" onClick={handleSubmitCategory}/>
                        </form>
                        <div className={styles['show-info']}>
                            <p
                                onMouseEnter={() => info.current.style.display = 'block'}
                                onMouseLeave={() => info.current.style.display = 'none'}
                            >Show info</p>
                            <p id="text" ref={info}>
                                <em>
                                    For example if you wanna learn the words from the ninth module of some learning book
                                    or you wanna learn the animals. Add category animals and add the word you wanna
                                    learn and their meaning. You'll get the definition you chose for the word
                                    and now you have to guess what the word was.
                                    <br/>
                                    <br/>
                                    In the end it's gonna look like this.
                                    We have Unit 1 with words and their definitions.
                                    And the whole practice and learning about these words can go here.
                                    In this way you can choose which module or category to learn and when,
                                    without having to look and search for words in notebook.
                                </em>
                            </p>
                        </div>
                    </div>
                    <div className={styles.right}>
                        <form onSubmit={handleSubmitWord}>
                            <label>Select a category and add a word to it with its definition:
                                <br/>
                                {wordExists && <h2 style={{
                                    color: 'red',
                                    fontWeight: 700,
                                }}>{wordExists}</h2>}
                                {renderCategories()}
                            </label>
                            <br/>
                            {wordError.name && <h2>{wordError.name.map(e=> <span style={errorStyles}>{e}</span>)}</h2>}
                            <label>
                                Word:
                                <br/>
                                <input type='text' value={word.name} onChange={e => {
                                    const name = e.target.value
                                    setWord(prevState => ({
                                        ...prevState,
                                        name
                                    }))
                                }
                                }/>
                            </label>
                            {wordError.definition && <h2>{wordError.definition.map(e=> <span style={errorStyles}>{e}</span>)}</h2>}
                            <label>
                                Definition:
                                <br/>
                                <textarea value={word.definition} onChange={e => {
                                    const definition = e.target.value
                                    setWord(prevState => ({
                                        ...prevState,
                                        definition
                                    }))
                                }}
                                />
                            </label>
                            <br/>
                            <input value="Add word" type='submit'/>
                        </form>
                        <Link to={`/user/profile/${userContext.id}/practice`}>
                            Go to practice learning words
                        </Link>
                    </div>
                </div>
            </div>
            <Footer/>
        </div>
    )
}
