import React, {useEffect, useState} from 'react'

import {MDBBtn} from "mdbreact"
import contentService from "../../services/contentService"
import {Link, Switch} from "react-router-dom"
import styles from "./Home.module.css"
import Footer from "../Footer/Footer";
import {loremIpsum} from "react-lorem-ipsum/dist/lorem-ipsum";
import Navigation from "../Navigation/Navigation";
import {Loading} from "../Loading/Loading";
import {trackPromise} from "react-promise-tracker";

const Home = ({isAuthenticated}) => {
    const [categories, setCategories] = useState([])

    useEffect(() => {
        const fetchCategories = async () => {
            const categoriesResponse = await contentService.fetchCategories()
            const categories = await categoriesResponse.data
            setCategories(categories)
        }

            fetchCategories()
    }, [])

    function renderCategories() {
        const element = <ul className={styles.list}> {
            categories.map((c, i) => {
                return <li key={i}>
                    <Link to={{pathname: createLink(c), name: c}}>
                        <h3>{c}</h3>
                    </Link>
                </li>
            })
        }
        </ul>
        return <div className={styles['content-container']}> {element}</div>
    }

    function createLink(name) {
        return 'category/' + name.toLocaleLowerCase().split(' ').join('-')
    }

    return (
        <div className={styles['home-container']}>
            <Navigation/>
            <h1>Welcome to Hello-English</h1>
            <h3>Get started by choosing a category you'd wish to get better with</h3>
            <h4>Don't mind the car in the background <div>☺</div></h4>
            {renderCategories()}
            {/*{!isAuthenticated ?*/}
            <div className={styles.links}>
                <Link to="/register">Register for more content</Link>
                <Link to="/login">Login for more ...</Link>
            </div>
            {/*: null  }*/}
            <div/>
            <Footer/>
        </div>
    )
}


export default Home