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
        setWordCategories(await response.data)
    }

    function getWord(words, name) {
        return words.filter(x => x.name === name)[0]
    }

    function renderCategories() {
        return wordCategories.map(x => <option key={x.id} value={x.id}>{x.name}</option>)
    }

    function handleChange(e) {
        
        setCategory(e.target.value)
    }

    function renderWords() {
        function showHideWord(name, field, action) {
            setWords(prev => {
                prev.forEach(w => {
                    if (w.name === name) {
                        w.isShown = action
                        w.inputDefinition = action ? w.definition : ''
                        field.value = w.inputDefinition
                        w.isGuessed = false
                        debugger
                    }
                })

                return [...prev]
            })
        }

        function showDefinition(e) {
            const {name, inputField} = getWordBoxData(e)
            showHideWord(name, inputField, true);
        }

        function clearDefinition(e) {
            const {name, inputField} = getWordBoxData(e)
            showHideWord(name, inputField, false)
        }

        function helpWithWord(e) {
            const {name, inputField} = getWordBoxData(e)
            inputField.style.border = ''
            inputField.style.backgroundColor = ''

            setWords(prev => {
                prev.forEach(w => {
                    if (w.name === name) {
                        if (w.inputDefinition && !w.definition.startsWith(w.inputDefinition)) {
                            w.inputDefinition = ''
                        }

                        const index = Math.max(1, w.inputDefinition.length + 1)
                        w.inputDefinition = w.definition.slice(0, index)
                        inputField.value = w.inputDefinition
                    }
                })

                return [...prev]
            })
        }

        // function manageBoxButtons(action, ...rest) {
        //     rest.forEach(i => i.disabled = action)
        // }

        function guessWord(e) {
            const {name, inputField} = getWordBoxData(e)
            const word = getWord(words, name)
            const isGuessed = word.definition === word.inputDefinition

            if (isGuessed) {
                inputField.style.border = ''
                inputField.style.backgroundColor = ''
            } else {
                inputField.style.border = '4px solid red'
                inputField.style.backgroundColor = '#fc7b7b'
            }

            setWords(prev => {
                prev.forEach(w => {
                    if (w.name === word.name) {
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
                        <label><span>{w.name}</span>:
                            <input
                                type="text"
                                onChange={handleDefinitionChange}
                                value={words.inputDefinition}
                                placeholder="definition..."
                            />
                            {w.isGuessed && <FontAwesomeIcon className={styles.icon} icon={faCheck}/>}
                        </label>
                        <div className={styles['word-buttons']}>
                            <input type='submit' value='guess' onClick={guessWord}/>
                            <input type='submit' value='help' onClick={helpWithWord}/>
                            <input type='submit' value='show' onClick={showDefinition}/>
                            <input type='submit' value='clear' onClick={clearDefinition}/>
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
                        x.isShown = false
                        x.inputDefinition = ''
                        return x
                    }
                )
        )
    }

    function getWordBoxData(e) {
        const rootElement = e.target.parentNode.parentNode.firstChild
        const name = rootElement.firstChild.textContent
        const inputDefinition = rootElement.lastChild.value
        const inputField = rootElement.querySelector('input[type=text]')
        const [guess, help, show, clear] = e.target.parentNode.parentNode.querySelectorAll('input[type=submit]')

        return {
            name,
            inputDefinition,
            inputField,
            guess,
            help,
            show,
            clear
        }
    }

    function handleDefinitionChange(e) {
        const {name} = getWordBoxData(e)
        const inputDefinition = e.target.value

        const {inputField} = getWordBoxData(e)
        inputField.style.border = ''
        inputField.style.backgroundColor = ''

        setWords(prev => {
            const newState = [...prev]
            let newDef = getWord(newState, name)
            newDef.inputDefinition = inputDefinition
            return newState
        })
    }

    function refreshWords() {

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
                    <input type="submit" value="Refresh" onClick={refreshWords}/>
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