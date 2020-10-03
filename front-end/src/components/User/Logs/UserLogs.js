import React, {Fragment, useEffect, useState} from "react"

import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import userService from "../../../services/UserService"
import {useHistory} from 'react-router-dom'
import TableComponent from "./LogTable";


export default function () {
    const [logs, setUserLogs] = useState([])
    const history = useHistory()

    useEffect(() => {
        fetchLogs()
    }, [])

    async function fetchLogs() {
        const url = history.location.pathname.substr(history.location.pathname.lastIndexOf('/')+1)
        const response = await userService.fetchUserLogs(url)
        const userLogs = await response.data
        debugger
        debugger
        setUserLogs(userLogs)
    }

    return (
        <Fragment>
            <Navigation/>
            <TableComponent title="User logs" to="/user/details/" logs={logs}/>
            <Footer/>
        </Fragment>
    )
}