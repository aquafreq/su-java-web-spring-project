import React from 'react';
import {Link} from 'react-router-dom';
import styles from './Navigation.module.css'
import {MDBDropdown, MDBDropdownItem, MDBDropdownMenu, MDBDropdownToggle} from "mdbreact";

const Navigation = ({user, logout}) => {
    return (
        <div>{user.username ? <strong style={{
            color: 'blue',
            border: 'solid 30px #eb34c9',
            background: 'linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(176,27,195,0.49653364763874297) 35%, rgba(0,212,255,0.5665616588432247) 100%)',
            fontSize: '-webkit-xxx-large',
        }}>Hello, {user.username}</strong> : null}

            <div className={styles.Nav}>
                {user.authorities.includes('ADMIN', 'MODERATOR') ?
                    <div className={styles.AdminNav}>
                        <MDBDropdown>
                            <MDBDropdownToggle caret color="secondary">
                                ADMINISTRATION
                            </MDBDropdownToggle>
                            <MDBDropdownMenu basic>
                                <Link to="/administration/create-content"><MDBDropdownItem>Create content</MDBDropdownItem></Link>
                                {
                                    user.authorities.includes('ADMIN') ?
                                        <> <MDBDropdownItem divider/>
                                            <Link to="/administration/manage-roles"><MDBDropdownItem>Manage
                                                roles</MDBDropdownItem></Link>
                                        </> : null
                                }
                            </MDBDropdownMenu>
                        </MDBDropdown>
                    </div> : null
                }
                <Link to="/"> <strong>Home</strong></Link>
                {user.authorities.length ? <Link to="/logout" onClick={logout}><strong>Logout</strong></Link>
                    : <>
                        <Link to="/login"><strong>Login</strong></Link>
                        <Link to="/register"><strong>Register</strong></Link>
                    </>
                }
            </div>
        </div>
    )
}

export default Navigation;