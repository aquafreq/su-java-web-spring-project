import React, {useCallback, useContext, useEffect, useMemo, useState} from "react"
import userService from "../../services/UserService"
import styles from './ManageRole.module.css'
import UserContext from "../../auth/UserContext"
import SearchBar from "./SearchBar/SearchBar"
import Navigation from "../Navigation/Navigation"
import Footer from "../Footer/Footer"
import {useHistory} from 'react-router-dom'

const ROLE_PREFIX = 'ROLE_'
const EMPTY_STRING = ''
const userId = localStorage.getItem('userId')

export default function () {
    const [users, setUsers] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [roles, setRoles] = useState({})
    const {id, userRoles} = useContext(UserContext)
    const history = useHistory()

    const Button = (style, handler, action, index) =>
        <span className={style} key={index}><button onClick={handler}>{action}</button></span>

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = async () => {
        Promise.all([userService.fetchAllUsers(), userService.fetchAllRoles()])
            .then(r => {
                const users =
                    r[0].data._embedded
                        .userResponseModelList
                        .filter(u => {
                                if (u.authorities.map(a => a.authority).includes('ROLE_ROOT_ADMIN')) {
                                    u.isRootAdmin = true
                                }

                                return u
                            }
                        )
                        .filter(u => u.id !== (id || userId))
                        .map(u => {
                            u.display = true
                            return u
                        })
                setUsers(users)

                const roles =
                    r[1]
                        .data
                        .reduce((acc, val) =>
                            (acc[val.id] = val.authority.replace(ROLE_PREFIX, EMPTY_STRING), acc), {})
                setRoles(roles)
                setIsLoading(false)
            })
    }

    function enableDisableUser({enabled, _links}) {
        const action = enabled ? 'Disable' : 'Enable'
        const link = enabled ? _links['disable-user'].href : _links['permit-user'].href

        async function updateUser(ev) {
            ev.preventDefault()

            const axiosResponse = await userService.updateUser(link)
            const updatedUser = await axiosResponse.data

            setUsers((users) => {
                const newUsers = [...users]
                let user = newUsers[getUserIndex(users, updatedUser.id)]
                user.enabled = updatedUser.enabled
                return newUsers
            })
        }

        const colorMap = {
            Disable: styles.disable,
            Enable: styles.enable,
        }

        return Button(colorMap[action], updateUser, action)
    }

    const roleHandler = url => {
        return async e => {
            e.preventDefault()
            const response = await userService.updateUser(url)
            const newUser = await response.data
            setUsers(users => {
                const newState = [...users]
                const oldUser = newState[getUserIndex(users, newUser.id)]
                oldUser.authorities = newUser.authorities
                oldUser._links = newUser._links
                return newState
            })
        }
    }

    const getLinkRole = element => roles[element.split('/').pop()]

    const renderRoleLink = ({href}, i = null) =>
        Button(styles.role, roleHandler(href), getLinkRole(href), i)

    const roleClickHandler = (element, {isRootAdmin}) => {
        if (element && !isRootAdmin)
            return Array.isArray(element) ?
                element.map((el, i) => renderRoleLink(el, i)) : renderRoleLink(element)
    }

    const renderUsers = (renderUsers) => {
        return renderUsers ? (
            <table className={styles.table}>
                <thead className={styles.tableHead}>
                <tr>
                    <th>Id</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Profile</th>
                    <th>Enable/Disable a user</th>
                    <th>Current roles</th>
                    <th>Add role</th>
                    <th>Remove role</th>
                </tr>
                </thead>
                <tbody>
                {
                    renderUsers.map(u => {
                        return u.display ? (<tr key={u.id} className={styles.tableRow}>
                            <td>{u.id}</td>
                            <td>{u.username}</td>
                            <td>{u.email}</td>
                            <td>
                                <button onClick={(() => {
                                    const url = u._links.self.href
                                    history.push(
                                        `/user/${url.substring(url.lastIndexOf('/') + 1)}/details`,
                                        url)
                                })}>Link
                                </button>
                            </td>
                            <td>{
                                !u.authorities.filter(a => a.authority !== 'ROLE_USER').length ? enableDisableUser(u) : null
                            }</td>
                            <td>{u.authorities.map((auth, i) =>
                                <span
                                    key={auth.id}> {roles[auth.id]}{i !== u.authorities.length - 1 ? ',\u00A0' : null}
                                </span>
                            )}
                            </td>
                            <td>{roleClickHandler(u._links['give-user-role'], u)}</td>
                            <td>{roleClickHandler(u._links['remove-user-role'], u)}</td>
                        </tr>) : null
                    })
                }
                </tbody>
            </table>
        ) : <div>Loading ...</div>
    }

    const filter = (value, filterCriteria) => {
        if (filterCriteria) {
            setUsers(prevState => {
                let newState = [...prevState]
                newState.forEach(u => {
                        u.display = !value.trim() || u[filterCriteria].startsWith(value)
                    }
                )

                return newState
            })
        }

        return renderUsers(users ? users : null)
    }

    return (
        <>
            <Navigation/>
            <div className={styles.container}>

                <SearchBar filter={filter}/>
                {!isLoading ? filter('', '') : <div>Loading ....</div>}
            </div>
            <Footer/>
        </>
    )
}

const getUserIndex = (users, id) => users.findIndex(u => u.id === id)

//todo searcr bar
//split logic User table component