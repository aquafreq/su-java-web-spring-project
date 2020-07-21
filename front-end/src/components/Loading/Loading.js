import React from 'react'

import styles from './Loading.module.css'

//this way you won't get the spinner staying in the response if you decide to debug

export const Loading = () => (
    <div className={styles.loadingWrap}>
        <div>
            {/*<img src="api/loading-spinner-icons8.gif" alt="imagine a fancy spinner"/>*/}
            <img src={process.env.PUBLIC_URL + '/loading-spinner-icons8.gif'} alt="imagine a fancy spinner"/>
        </div>
    </div>
)

