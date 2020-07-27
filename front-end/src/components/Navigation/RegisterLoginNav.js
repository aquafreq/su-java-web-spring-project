import styles from "../Home/Home.module.css";
import {Link} from "react-router-dom";
import React, {useContext} from "react";
import UserContext from "../../auth/UserContext";

export default function (location) {
    const userContext = useContext(UserContext)
    return !userContext.id ? (
        <div className={styles.links}>
            {location.path === '/' ?
                <>
                    <Link to="/register">Register for more content</Link>
                    <Link to="/login">Login for more ...</Link>
                </> :
                <>
                    {location.path === '/login' && <Link to="/register">Register for more content</Link>}
                    {location.path === '/register' && <Link to="/login">Login for more ...</Link>}
                </>
            }
        </div>) : null
}