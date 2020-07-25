import React, {useEffect, useState} from 'react'

import {MDBBtn} from "mdbreact"
import contentService from "../../services/contentService"
import {Link, Switch, useHistory} from "react-router-dom"
import styles from "./Home.module.css"
import Footer from "../Footer/Footer"
import {loremIpsum} from "react-lorem-ipsum/dist/lorem-ipsum"
import Navigation from "../Navigation/Navigation"
import RegisterLoginNav from "../Navigation/RegisterLoginNav"

const Home = ({isAuthenticated}) => {
    const history = useHistory()
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
            categories.map(({name}, i) => {
                return <li key={i}>
                    <Link to={{pathname: createLink(name), name: name}}>
                        <h3>{name}</h3>
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
        <>
            <Navigation/>
            <div className={styles['home-container']}>
                <h1>Welcome to Hello-English</h1>
                <h3>Get started by choosing a category you'd wish to get better with</h3>
                <h4>Don't mind the car in the background <div>☺</div></h4>
                {renderCategories()}
                <RegisterLoginNav path={history.location.pathname}/>
                <div/>
            </div>
            <Footer/>
        </>
    )
}


export default Home