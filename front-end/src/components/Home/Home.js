import React, {useEffect, useState} from 'react'

import {MDBBtn} from "mdbreact"
import contentService from "../../services/contentService"
import {Link, Switch, useHistory} from "react-router-dom"
import styles from "./Home.module.css"
import Footer from "../Footer/Footer"
import Navigation from "../Navigation/Navigation"
import RegisterLoginNav from "../Navigation/RegisterLoginNav"
import {Loading} from "../Loading/Loading";

const Home = () => {
    const history = useHistory()
    const [categories, setCategories] = useState([])
    // const [isLoading, setIsLoading] = useState(false)

    useEffect(() => {
        const fetchCategories = async () => {
            const categoriesResponse = await contentService.fetchCategories()
            const categories = await categoriesResponse.data
            setCategories(categories)
            // setTimeout(() => setIsLoading(false), 400)
        }

        // setIsLoading(true)
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
                <br/>
                {renderCategories()}
                <RegisterLoginNav path={history.location.pathname}/>
                <div/>
            </div>
            <Footer/>
        </>
    )
}


export default Home