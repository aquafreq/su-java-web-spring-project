import React, {Fragment, useEffect, useState} from "react"

import Navigation from "../../Navigation/Navigation"
import Footer from "../../Footer/Footer"
import userService from "../../../services/UserService"
import TableComponent from "./LogTable";

export default function () {
    const [logs, setLogs] = useState([])

    useEffect(() => {
        fetchLogs()
    }, [])

    async function fetchLogs() {
        const response = await userService.fetchLogs()
        const logs = await response.data
        setLogs(logs)
    }

    return (
        <Fragment>
            <Navigation/>
            <TableComponent title="All logs" to="/administration/logs/" logs={logs}/>
            <Footer/>
        </Fragment>
    )
}