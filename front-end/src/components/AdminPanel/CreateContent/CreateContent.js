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
    const [description, setDescription] = useState('')
    const [title, setTitle] = useState('')
    const [category, setCategory] = useState('')
    const [level, setLevel] = useState('')
    const [levels, setLevels] = useState([])
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
        contentService
            .createCategory(category)
            .then(() => alert('category ' + category + ' creaed successful'))
            .then(() => {
                setCategory('')
                setIsSent(true)
            })
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

        contentService.createContent(content)
            .then(() => {
                setTitle('')
                setDescription('')
            })
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
                                            <MDBIcon far icon="paper-plane" className="ml-2"/>
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
                        <h3>Choose category for an exercise or a content</h3>
                        <select className="browser-default custom-select"
                                ref={chosenCategory}
                                required
                        >
                            <option>Choose category</option>
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
                                                <MDBIcon far icon="paper-plane" className="ml-2"/>
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