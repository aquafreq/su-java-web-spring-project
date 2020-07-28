import React, {useContext, useEffect, useState} from "react"
import {useHistory} from 'react-router-dom'
import userService from "../../../services/UserService"
import contentService from "../../../services/contentService"

import styles from './Content.module.css'
import {loremIpsum, username, Avatar, fullname} from "react-lorem-ipsum"
import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import UserContext from "../../../auth/UserContext";
import {Loading} from "../../Loading/Loading";

export default function () {
    const history = useHistory()
    const [comments, setComments] = useState([])
    const [comment, setComment] = useState('')
    const [content, setContent] = useState({})
    const [isLoading, setIsLoading] = useState(true)
    const userContext = useContext(UserContext)

    useEffect(() => {
        fetchData()
    }, [])

    function fetchData() {
        const urlString = history.location.pathname

        debugger

        contentService
            .getContent(urlString)
            .then(r => {
                debugger
                setComments(r.data.comments)
                setContent(
                    {
                        id: r.data.id, title: r.data.title, description: r.data.description,
                        author: r.data.author.username, authorId: r.data.author.id,
                        authorEmail: r.data.author.email,
                        category: r.data.categoryName, categoryId: r.data.categoryId
                    }
                )
            })
            .then(() => setIsLoading(false))
    }


    //renders text into paragraphs
    function renderContent() {
        return (
            <div>
                <h2>{content.title}</h2>
                <article>
                    {content
                        .description
                        .split('.')
                        .reduce((acc, val, i) => {
                            let sentences = i === content
                                .description
                                .split('.').length - 1 ? val : val + '.'

                            if (i % 4 === 0) {
                                sentences = <p key={i}>{sentences}<br/></p>
                            }

                            return [...acc, sentences]
                        }, [])
                        // .map((text, i) => {
                        //     return (<p key={i}>{text}<br/></p>)
                        // })
                    }
                </article>
            </div>
        )
    }

    function allComments() {
        return (
            <div>{
                comments
                    .map(c => {
                        const user = c.userUsername || 'Anonymous'
                        const message = user + ' : ' + c.message
                        return <textarea disabled defaultValue={message} key={c.id}/>
                    })}
            </div>
        )
    }

    function renderComments() {
        return comments.length ?
            <div>{allComments()}</div> : <h5>Be the first to comment !</h5>
    }

    if (isLoading) return <Loading/>

    function handleSubmitComment(e) {
        e.preventDefault()

        const urlPath = history.location.pathname
        const userComment = {
            message: comment,
            contendId: content.id,
            categoryId: content.categoryId,
            userId: userContext.id || 'Anonymous',
        }

        setIsLoading(true)

        contentService
            .addCommentToContent(urlPath, userComment)
            .then(c => {
                setComments(prevState => [...prevState, c.data])
                setComment('')
            })
            .finally(() => {
                setIsLoading(false)
            })
    }


    return (
        <>
            <Navigation/>
            <div className={styles['main-container']}>
                {isLoading ? <Loading/> :
                    <>
                        {renderContent()}
                        <div className={styles['comment-section']}>
                            <h5>Comments section</h5>
                            <div className={styles.comments}>
                                {renderComments()}
                            </div>
                            <section className={styles['my-comment']}>
                                <label>
                                    <p>Was it helpful ?</p>
                                    <form>
                                    <textarea
                                        placeholder="Give us your thoughts..."
                                        value={comment}
                                        onChange={e => setComment(e.target.value)}
                                    />
                                        <input value="Send" type="submit" onClick={handleSubmitComment}/>
                                    </form>
                                </label>
                            </section>
                        </div>
                    </>
                }

            </div>
            <Footer/>
        </>
    )
}