import React from 'react'

import styles from '../NotFound/NotFound.module.css'
import {Link} from "react-router-dom";

const sthWentWrongMessage = <p>
    <bold>☹ 404 Something went wrong...</bold><br/>
    Press <span><Link to="/">♥</Link></span> to go home</p>

const NotFound = () => {
    return (
        <div className={styles.error}>
            {sthWentWrongMessage}
            {sthWentWrongMessage}
            {sthWentWrongMessage}
            {sthWentWrongMessage}
        </div>
    )
}

export default NotFound