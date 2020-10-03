import TableContainer from "@material-ui/core/TableContainer"
import Paper from "@material-ui/core/Paper"
import Table from "@material-ui/core/Table"
import TableHead from "@material-ui/core/TableHead"
import TableBody from "@material-ui/core/TableBody"
import TableRow from "@material-ui/core/TableRow"
import TableCell from "@material-ui/core/TableCell"
import TablePagination from "@material-ui/core/TablePagination"
import React from "react"
import {Link, useHistory} from "react-router-dom"
import {makeStyles} from "@material-ui/core/styles"
import styles from './TableComponents.module.css'

export default function ({title, logs, to}) {
    const [page, setPage] = React.useState(0)
    const [rowsPerPage, setRowsPerPage] = React.useState(10)

    const handleChangePage = (event, newPage) => {
        setPage(newPage)
    }

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10))
        setPage(0)
    }

    const useStyles = makeStyles({
        table: {
            minWidth: 650,
        },
    })

    const classes = useStyles()

    function renderTableHead() {
        return (
            <TableRow>
                <TableCell align="center">User</TableCell>
                <TableCell align="center">Action</TableCell>
                <TableCell align="center">URL</TableCell>
                <TableCell align="center">Occurrence</TableCell>
            </TableRow>
        )
    }

    const link = row => `${to}${to.includes('details') ? row.userId : row.username}`

    const emptyRows = rowsPerPage - Math.min(rowsPerPage, logs.length - page * rowsPerPage)

    function renderTableBody() {
        return <>
            {logs
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row) => (
                    <TableRow key={row.id}>
                        <TableCell align="center" scope="row">
                            <Link to={link(row)}>{row.username}</Link>
                        </TableCell>
                        <TableCell size="medium" align="center">{row.method}</TableCell>
                        <TableCell align="center">{row.url}</TableCell>
                        <TableCell align="center">{row.occurrence}</TableCell>
                    </TableRow>
                ))}
        </>
    }

    return (
        <>
            <TableContainer component={Paper}>
                <h1 className={styles.header}>{title}</h1>
                <Table className={classes.table} aria-label="simple table">
                    <TableHead>
                        {renderTableHead()}
                    </TableHead>
                    <TableBody>
                        {renderTableBody()}
                        {emptyRows > 0 && (
                            <TableRow style={{height: 53 * emptyRows}}>
                                <TableCell colSpan={6}/>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="section"
                    count={logs.length}
                    page={page}
                    onChangePage={handleChangePage}
                    rowsPerPage={rowsPerPage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                />
            </TableContainer>
        </>
    )
}