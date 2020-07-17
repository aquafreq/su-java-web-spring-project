import React, {useState} from "react"

import styles from './SearchBar.module.css'

export default function SearchBar({filter}) {
    const [value, setValue] = useState('')
    const [filterCriteria, setFilterCriteria] = useState('username')

    return (
        <div className={styles.SearchBar}>
            <section>
                <h5>Filter users by:</h5>
                <input
                    type='text' placeholder='Enter value...'
                       value={value} onChange={e=> {
                    setValue(e.target.value)
                    filter(e.target.value, filterCriteria)
                }}/>
                <select onChange={(e) => setFilterCriteria(e.target.value)}>
                    <option value="username">Username</option>
                    <option value="email">Email</option>
                </select>
            </section>
        </div>
    )
}