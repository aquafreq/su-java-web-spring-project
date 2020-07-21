import React, {useEffect, useLayoutEffect, useRef, useState} from "react"

import contentService from "../../../services/contentService";
import {MDBBtn, MDBCard, MDBCardBody, MDBCol, MDBContainer, MDBIcon, MDBInput, MDBRow} from "mdbreact";
import styles from './CreateContent.module.css'
import {loremIpsum, LoremIpsum, name, username} from "react-lorem-ipsum"
import Navigation from "../../Navigation/Navigation";
import Footer from "../../Footer/Footer";

//TODO da sa oprai



export default function CreateContent() {
    const [sent, setIsSent] = useState(false)
    const [categories, setCategories] = useState([])
    const [description, setDescription] = useState('')
    const [title, setTitle] = useState('')
    const inputRef = useRef('')

    useLayoutEffect(() => {
        contentService
            .fetchCategories()
            .then(r => setCategories(r.data))
            .then(() => setIsSent(false))
    }, [sent])

    const handleCategorySubmit = (e) => {
        e.preventDefault()

        const value = inputRef.current.value;
        contentService.createCategory(value)
            .then(() => alert('category ' + value + ' sent successful'))
            .then(() => setIsSent(true))
        inputRef.current.value = ''
    }

    function handleSubmit(e) {
        e.preventDefault()
        //validate
        debugger
        debugger
        contentService.createContent({title, description})
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
                                        ref={inputRef}
                                        type="text"
                                        id="defaultFormCardNameEx"
                                        className="form-control"/>
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
                                onChange={(e) => e.target.value}
                                required
                        >
                            <option>Choose category</option>
                            {categories.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
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
                                        <div className="text-center py-4 mt-3">
                                            <MDBBtn
                                                onClick={handleSubmit}
                                                className="btn btn-outline-purple" type="submit">
                                                Add content
                                                <MDBIcon far icon="paper-plane" className="ml-2"/>
                                            </MDBBtn>
                                        </div>

                                    </MDBCardBody>
                                </MDBCard>
                            </MDBCol>
                        </MDBRow>
                        {/*<MDBRow className={styles.block}>*/}
                        {/*    <MDBCol md="6">*/}
                        {/*        <MDBCard>*/}
                        {/*            <MDBCardBody>*/}
                        {/*                <form>*/}
                        {/*                    <p className="h4 text-center py-4">Add exercise for chosen category</p>*/}
                        {/*                    <label*/}
                        {/*                        htmlFor="name"*/}
                        {/*                        className="grey-text font-weight-light"*/}
                        {/*                    >*/}
                        {/*                        Exercise name*/}
                        {/*                    </label>*/}
                        {/*                    <input*/}
                        {/*                        type="text"*/}
                        {/*                        id="name"*/}
                        {/*                        className="form-control"*/}
                        {/*                    />*/}
                        {/*                    <br/>*/}
                        {/*                    <label*/}
                        {/*                        htmlFor="defaultFormCardEmailEx"*/}
                        {/*                        className="grey-text font-weight-light"*/}
                        {/*                    >*/}
                        {/*                       To do lol*/}
                        {/*                    </label>*/}
                        {/*                    <input*/}
                        {/*                        type="email"*/}
                        {/*                        id="defaultFormCardEmailEx"*/}
                        {/*                        className="form-control"*/}
                        {/*                    />*/}
                        {/*                    <div className="text-center py-4 mt-3">*/}
                        {/*                        <MDBBtn className="btn btn-outline-purple" type="submit">*/}
                        {/*                            Send*/}
                        {/*                            <MDBIcon far icon="paper-plane" className="ml-2"/>*/}
                        {/*                        </MDBBtn>*/}
                        {/*                    </div>*/}
                        {/*                </form>*/}
                        {/*            </MDBCardBody>*/}
                        {/*        </MDBCard>*/}
                        {/*    </MDBCol>*/}
                        {/*</MDBRow>*/}
                    </MDBContainer>
                </div>
            </form>
            <Footer/>
        </div>
    )
}

//import { LoremIpsum, Avatar } from 'react-lorem-ipsum';
//import { loremIpsum, name, surname, fullname, username } from 'react-lorem-ipsum';