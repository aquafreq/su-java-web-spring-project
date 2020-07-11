import React, {useEffect, useLayoutEffect, useRef, useState} from "react"

import contentService from "../../../services/contentService";
import {MDBBtn, MDBCard, MDBCardBody, MDBCol, MDBContainer, MDBIcon, MDBInput, MDBRow} from "mdbreact";
import styles from './CreateContent.module.css'


//TODO da sa oprai

export default function CreateContent() {
    const [sent, setIsSent] = useState(false)
    const [categories, setCategories] = useState([])
    const [category, setCategory] = useState('')
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
        contentService.addCategory(value)
            .then(() => alert('category ' + value + ' sent successful'))
            .then(() => setIsSent(true))
        inputRef.current.value = ''
    }

    return (
        <div className={styles.Body}>
            <div className={styles.AddCategory}>
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
            </div>

            <div className={styles.ChooseCategory}>
                <div>
                    <h3>Choose category for exercise</h3>
                    <select className="browser-default custom-select"
                            onChange={(e) => setCategory(e.target.value)}
                    >
                        <option>Choose category</option>
                        {categories.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
                    </select>
                </div>
            </div>

            <div className={styles.AddExercise}>
                <MDBContainer>
                    <MDBRow className={styles.Row}>
                        <MDBCol md="6">
                            <MDBCard>
                                <MDBCardBody>
                                    <form>
                                        <p className="h4 text-center py-4">Add content</p>
                                        <label
                                            htmlFor="defaultFormCardNameEx"
                                            className="grey-text font-weight-light"
                                        >
                                            Your name
                                        </label>
                                        <input
                                            type="text"
                                            id="defaultFormCardNameEx"
                                            className="form-control"
                                        />
                                        <br/>
                                        <label
                                            htmlFor="defaultFormCardEmailEx"
                                            className="grey-text font-weight-light"
                                        >
                                            Your email
                                        </label>
                                        <input
                                            type="email"
                                            id="defaultFormCardEmailEx"
                                            className="form-control"
                                        />
                                        <div className="text-center py-4 mt-3">
                                            <MDBBtn className="btn btn-outline-purple" type="submit">
                                                Send
                                                <MDBIcon far icon="paper-plane" className="ml-2"/>
                                            </MDBBtn>
                                        </div>
                                    </form>
                                </MDBCardBody>
                            </MDBCard>
                        </MDBCol>
                    </MDBRow>
                </MDBContainer><MDBContainer>
                <MDBRow className={styles.Row}>
                    <MDBCol md="6">
                        <MDBCard>
                            <MDBCardBody>
                                <form>
                                    <p className="h4 text-center py-4">Add exercise</p>
                                    <label
                                        htmlFor="defaultFormCardNameEx"
                                        className="grey-text font-weight-light"
                                    >
                                        Your name
                                    </label>
                                    <input
                                        type="text"
                                        id="defaultFormCardNameEx"
                                        className="form-control"
                                    />
                                    <br/>
                                    <label
                                        htmlFor="defaultFormCardEmailEx"
                                        className="grey-text font-weight-light"
                                    >
                                        Your email
                                    </label>
                                    <input
                                        type="email"
                                        id="defaultFormCardEmailEx"
                                        className="form-control"
                                    />
                                    <div className="text-center py-4 mt-3">
                                        <MDBBtn className="btn btn-outline-purple" type="submit">
                                            Send
                                            <MDBIcon far icon="paper-plane" className="ml-2"/>
                                        </MDBBtn>
                                    </div>
                                </form>
                            </MDBCardBody>
                        </MDBCard>
                    </MDBCol>
                </MDBRow>
            </MDBContainer>
            </div>
        </div>
    )
}
