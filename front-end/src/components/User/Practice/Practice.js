import React, {Fragment, useEffect, useState} from "react"

import styles from './Practice.module.css'
import {useHistory} from 'react-router-dom'
import userService from "../../../services/UserService"
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faCheck} from '@fortawesome/free-solid-svg-icons'

export default function () {
    const [wordCategories, setWordCategories] = useState([])
    const [category, setCategory] = useState('')
    const [words, setWords] = useState([])
    const [error, setError] = useState({hasError: false, message: ''})

    const history = useHistory()

    useEffect(() => {
        fetchData()
    }, [])

    async function fetchData() {
        const url = history.location.pathname
        const response = await userService.fetchUserCategoryWords(url)
        debugger
        setWordCategories(await response.data)
    }

    function getWord(words, definition) {
        return words.filter(x => x.definition === definition)[0]
    }

    function renderCategories() {
        return wordCategories.map(x => <option key={x.id} value={x.id}>{x.name}</option>)
    }

    function handleChange(e) {
        setCategory(e.target.value)
    }

    function setInputCSS(inputField, border = '', background = '') {
        inputField.style.border = border
        inputField.style.backgroundColor = background
    }

    function renderWords() {
        function showHideWord(definition, field, action) {
            setWords(prev => {
                prev.forEach(w => {
                    if (w.definition === definition) {
                        w.inputName = action ? w.name : ''
                        field.value = w.inputName
                        w.isGuessed = false
                    }
                })

                return [...prev]
            })
        }

        function showWord(e) {
            const {definition, inputField} = getWordBoxData(e)
            showHideWord(definition, inputField, true);
        }

        function clearWord(e) {
            const {definition, inputField} = getWordBoxData(e)
            showHideWord(definition, inputField, false)
        }

        function helpWithWord(e) {
            const {definition, inputField} = getWordBoxData(e)
            setInputCSS(inputField)

            setWords(prev => {
                prev.forEach(w => {
                    if (w.definition === definition) {
                        if (w.inputName && !w.name.startsWith(w.inputName)) {
                            w.inputName = ''
                        }

                        const index = Math.max(1, w.inputName.length + 1)
                        w.inputName = w.name.slice(0, index)
                        inputField.value = w.inputName
                    }
                })

                return [...prev]
            })
        }

        // function manageBoxButtons(action, ...rest) {
        //     rest.forEach(i => i.disabled = action)
        // }

        function guessWord(e) {
            const {definition, inputField} = getWordBoxData(e)
            const word = getWord(words, definition)
            const isGuessed = word.name === word.inputName

            if (isGuessed) {
                setInputCSS(inputField)
            } else {
                setInputCSS(inputField, '4px solid red', '#fc7b7b')
            }

            setWords(prev => {
                prev.forEach(w => {
                    if (w.name === word.inputName) {
                        word.isGuessed = isGuessed
                    }
                })
                return [...prev]
            })

        }

        const a = category && wordCategories.filter(c => c.id === category)[0].words.length === 0

        return (category && a) ?
            <h3>No words for this category yet!</h3> : words.map(w => {
                return (
                    <section key={w.id} className={styles['word-section']}>
                        <label><span>{w.definition}</span>:&nbsp;
                            <input
                                type="text"
                                onChange={handleWordNameChange}
                                value={w.inputName}
                                placeholder="definition..."
                            />
                            {w.isGuessed && <FontAwesomeIcon className={styles.icon} icon={faCheck}/>}
                        </label>
                        <div className={styles['word-buttons']}>
                            <input type='submit' value='guess' onClick={guessWord}/>
                            <input type='submit' value='help' onClick={helpWithWord}/>
                            <input type='submit' value='show' onClick={showWord}/>
                            <input type='submit' value='clear' onClick={clearWord}/>
                        </div>
                    </section>
                )
            })
    }

    function renderUI(error, renderFunc, e) {
        if (!error.hasError) {
            setError({hasError: false, message: ''})
            e.style.border = ''

            renderFunc()
        } else {
            e.style.border = '4px solid red'
            setError(error)
        }
    }


    function beginPractice(e) {
        const error = {
            hasError: !category,
            message: 'Please select a category or create one!'
        }

        renderUI(error, mapWords, e.target)
    }

    function mapWords() {
        setWords(
            wordCategories
                .filter(c => c.id === category)
                .pop()
                .words
                .map(x => {
                        x.isGuessed = false
                        x.inputName = ''
                        return x
                    }
                )
        )
    }

    function getWordBoxData(e) {
        const rootElement = e.target.parentNode.parentNode.firstChild
        const definition = rootElement.firstChild.textContent
        const inputName = rootElement.lastChild.value
        const inputField = rootElement.querySelector('input[type=text]')
        const [guess, help, show, clear] = e.target.parentNode.parentNode.querySelectorAll('input[type=submit]')

        return {
            definition,
            inputName,
            inputField,
            guess,
            help,
            show,
            clear
        }
    }

    function handleWordNameChange(e) {
        const {inputField} = getWordBoxData(e)
        inputField.style.border = ''
        inputField.style.backgroundColor = ''

        const {definition} = getWordBoxData(e)

        const inputName = e.target.value

        setWords(prev => {
            const newState = [...prev]
            let newDef = getWord(newState, definition)
            newDef.inputName = inputName
            debugger
            return newState
        })
    }

    //https://stackoverflow.com/questions/6274339/how-can-i-shuffle-an-array
    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }

    function refreshWords(e) {
        e.target
            .offsetParent
            .querySelectorAll('input[type=text]')
            .forEach(x => setInputCSS(x))

        setWords(prevState => {
            let arr = prevState.map(w => {
                w.inputName = ''
                return w
            })

            arr.forEach(v => v.isGuessed = false)

            return [...shuffleArray(arr)]
        })
    }

    function showCategoryWithWords() {

    }

    return (
        <Fragment>
            <Navigation/>
            <div className={styles['practice-container']}>
                <h1>Pick a category and start practicing the words in it!</h1>
                <span className={styles.error}>
                {error.hasError && <h5>{error.message}</h5>}
                </span>
                <div>
                    <select onChange={handleChange}>
                        <option value=''/>
                        {renderCategories()}
                    </select>
                    <input type="submit" value="Begin" onClick={beginPractice}/>
                    <input type="submit" value="Shuffle" onClick={refreshWords}/>
                    <input type="submit"
                           value="Display all categories and words within them"
                           onClick={showCategoryWithWords}
                    />
                </div>
                <div>
                    <div className={styles.words}>
                        {words && renderWords()}
                    </div>
                </div>
            </div>
            <Footer/>
        </Fragment>
    )
}