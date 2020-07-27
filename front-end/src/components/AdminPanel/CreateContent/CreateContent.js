import React, {useContext, useLayoutEffect, useRef, useState} from "react"

import contentService from "../../../services/contentService"
import {MDBBtn, MDBCard, MDBCardBody, MDBCol, MDBContainer, MDBIcon, MDBRow} from "mdbreact"
import styles from './CreateContent.module.css'
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import UserContext from "../../../auth/UserContext"

export default function CreateContent() {
    const userContext = useContext(UserContext)
    const [sent, setIsSent] = useState(false)
    const [categories, setCategories] = useState([])
    const [categoryError, setCategoryError] = useState('')
    const [description, setDescription] = useState('')
    const [title, setTitle] = useState('')
    const [category, setCategory] = useState('')
    const [level, setLevel] = useState('')
    const [levels, setLevels] = useState([])
    const [titleError, setTitleError] = useState('')
    const [chosenCategoryError, setChosenCategoryError] = useState('')
    const [contentError, setContentError] = useState('')
    const chosenCategory = useRef('')

    useLayoutEffect(() => {
        Promise.all(
            [contentService.fetchCategories(),
                contentService.fetchGrammarLevels()])
            .then(([categories, levels]) => {
                setCategories(categories.data)
                setLevels(levels.data)
                setLevel(levels.data[0] || '')
            })
            .then(() => setIsSent(false))
    }, [sent])

    const handleCategorySubmit = (e) => {
        e.preventDefault()

        if (category.length > 4) {
            setCategoryError('')
            contentService
                .createCategory(category)
                .then(() => alert('category ' + category + ' creaed successful'))
                .then(() => {
                    setCategory('')
                    setIsSent(true)
                })
        } else {
            setCategoryError('Category name must be at least 5 characters!')
        }

    }

    function handleSubmit(e) {
        e.preventDefault()

        const content = {
            authorId: userContext.id,
            title,
            description,
            categoryId: chosenCategory.current.value,
            difficulty: level
        }

        debugger
        if (!chosenCategory.current.value) {
            setChosenCategoryError('Category must not be empty!')
        } else {
            setChosenCategoryError('')
        }

        if (!title || title.length < 5) {
            setTitleError('Title should be more than 5 characters!')
        } else {
            setTitleError('')
        }

        if (!description) {
            setContentError('Fill some correct description !')
        } else {
            setContentError('')
        }

        if (!chosenCategoryError && !titleError && !contentError) {
            debugger
            contentService.createContent(content)
                .then(() => {
                    setTitle('')
                    setDescription('')
                })
        }
    }

    return (
        <div className={styles.body}>
            <Navigation/>
            <MDBContainer>
                <MDBRow>
                    <MDBCol md="12">
                        <MDBCard>
                            <MDBCardBody>
                                <form>
                                    {categoryError && <h2 className={styles['category-error']}>{categoryError}</h2>}
                                    <p className="h4 text-center py-4">Add category</p>
                                    <label
                                        htmlFor="defaultFormCardNameEx"
                                        className="grey-text font-weight-light">
                                        <strong>
                                            Add a new English grammar category to the existing ones:
                                        </strong>
                                    </label>
                                    <input
                                        value={category}
                                        type="text"
                                        id="defaultFormCardNameEx"
                                        className="form-control"
                                        onChange={e => setCategory(e.target.value)}
                                    />
                                    <div className="text-center py-4 mt-3">
                                        <MDBBtn className="btn btn-outline-purple" type="submit"
                                                onClick={handleCategorySubmit}
                                        >
                                            Add
                                        </MDBBtn>
                                    </div>
                                </form>
                            </MDBCardBody>
                        </MDBCard>
                    </MDBCol>
                </MDBRow>
            </MDBContainer>
            <form>
                <div className={styles.chooseCategory}>
                    <div>
                        {chosenCategoryError && <h2 className={styles['category-error']}>{chosenCategoryError}</h2>}
                        <h3>Choose category for an exercise or a content</h3>
                        <select className="browser-default custom-select"
                                ref={chosenCategory}
                                required
                        >
                            <option value=''/>
                            {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                        </select>
                    </div>
                </div>
                <div className={styles.addContent}>
                    <MDBContainer>
                        <MDBRow className={styles.block}>
                            <MDBCol md="6">
                                <MDBCard>
                                    <MDBCardBody>
                                        {titleError && <h2 className={styles['category-error']}>{titleError}</h2>}
                                        <p className="h4 text-center py-4">Add content to chosen category</p>
                                        <label
                                            htmlFor="title"
                                            className="grey-text font-weight-light"
                                        >
                                            Title of content
                                        </label>
                                        <input
                                            type="text"
                                            id="title"
                                            className="form-control"
                                            value={title}
                                            onChange={(e) => setTitle(e.target.value)}
                                        />
                                        <br/>
                                        {contentError && <h2 className={styles['category-error']}>{contentError}</h2>}
                                        <label
                                            htmlFor="description"
                                            className="grey-text font-weight-light"
                                        >
                                            Description
                                        </label>
                                        <textarea
                                            id="description"
                                            className="form-control"
                                            value={description}
                                            onChange={(e) => setDescription(e.target.value)}
                                        />
                                        <label>
                                            Select the difficulty level of the uploaded lesson
                                            <select onChange={e => setLevel(e.target.value)}>
                                                {levels.map((l, i) => <option key={i} value={l}>{l}</option>)}
                                            </select>
                                        </label>
                                        <div className="text-center py-4 mt-3">
                                            <MDBBtn
                                                onClick={handleSubmit}
                                                className="btn btn-outline-purple" type="submit">
                                                Upload lesson
                                                <i className="fab fa-react" size='10px'/>
                                                {/*<MDBIcon icon={<i className="fab fa-react"></i>} size='10px'/>*/}
                                            </MDBBtn>
                                        </div>
                                    </MDBCardBody>
                                </MDBCard>
                            </MDBCol>
                        </MDBRow>
                    </MDBContainer>
                </div>
            </form>
            <Footer/>
        </div>
    )
}

//import { LoremIpsum, Avatar } from 'react-lorem-ipsum'
//import { loremIpsum, name, surname, fullname, username } from 'react-lorem-ipsum'