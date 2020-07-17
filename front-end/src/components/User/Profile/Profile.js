import React, {useState} from "react"

import styles from 'Profile.module.css'
import UserContext from "../../../auth/UserContext";
import userService from "../../../services/UserService";

export default function () {
    const [words, setWords] = useState([])
    const [user, setUser] = useState({})
    const history = useHistory()

    useEffect(async() => {
        userService.
        setUser()
    },[])


    return (
        <div className={styles.userProfile}>
            <h1>
                heelloo its profile page l((+__
            </h1>
            ne6to drugo ... rest
            <div className={styles.game}>
                mnoo top igra br, qkata e
                word = new word,
                dog = let6to kuè
            </div>
            <div>
                ma balgarin sam, ne sam ciganin,
                moe da sam barkaski cigagnin, ma sa balgarin batko,
            </div>
        </div>
    )
}
