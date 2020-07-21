import React, {useEffect, useState} from "react"

import {Link, useHistory} from 'react-router-dom'

import contentService from "../../../services/contentService"
import userService from "../../../services/UserService"
import styles from './AllContent.module.css'
import Navigation from "../../Navigation/Navigation";
import Footer from "../../Footer/Footer";
import {Loading} from "../../Loading/Loading"

export default function ({userId}) {
    const [category, setCategory] = useState({})
    const [isLoading, setIsLoading] = useState(true)
    const history = useHistory()

    function linkName(id) {
        return id.toLocaleLowerCase().split(' ').join('-');
    }

    useEffect(() => {
        const name = history.location.pathname

        contentService
            .getCategoryContent(linkName(name))
            .then(r => setCategory(r.data))
            .finally(() => setIsLoading(false))
    }, [])

    function getPathname(name) {
        return name.toLocaleLowerCase().split(' ').join('-');
    }

    const renderContent = () => {

        if (isLoading) return <Loading/>

        if (!Object.keys(category).length  || !category.content.length) return <div className={styles['no-content']}><h3>
            <Link to="/">Sorry...<br/> No content found for this topic!
                <br/>
                Press <span>♦</span> to go back home
            </Link></h3>
        </div>

        // if (!Object.keys(category).length || !category.content.length) return <div className={styles['no-content-h']}>
        //     <h3>Sorry... There's no content yet on {category.name}</h3>
        //     <h4><Link to="/">Go back</Link></h4>
        // </div>

        return <div className={styles.content}>
            <h3>Currently available resources</h3>
            <p>What you can read about {category.name}</p>
        {
                category.content.map(content => {
                        return (
                            <div key={content.id}>
                                <h3>
                                    <Link
                                        to={{
                                            pathname: `${getPathname(content.categoryName)}/${getPathname(content.title)}`,
                                            categoryId: content.categoryId, contentId: content.id
                                        }}>
                                        {content.title}
                                    </Link>
                                </h3>
                                <br/>
                                <em>by author:
                                    <Link to={{pathname: `/user/profile/${content.author.id}`, contentId: content.id}}>
                                        {content.author.username}
                                    </Link>
                                </em>
                            </div>
                        )
                    }
                )
            }
        </div>
    }

    return (
        <div className={styles['all-content']}>
            <Navigation/>
            <section>
                {renderContent()}
            </section>
            <Footer/>
        </div>
    )
}