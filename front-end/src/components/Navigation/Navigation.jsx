import React from 'react';
import {Link} from 'react-router-dom';
import styles from './Navigation.module.css'
import {MDBDropdown, MDBDropdownItem, MDBDropdownMenu, MDBDropdownToggle} from "mdbreact";

const Navigation = ({user, logout}) => {
    const hasRoleAdmin = !!user.authorities.filter(r => r.authority === 'ROLE_ADMIN').length
    const hasAnyRole = user.authorities.length > 1;

    function username() {
        return user.username ?
            <span> <strong>Hello, {user.username}</strong> </span> : null
    }

    function administrationBar() {
        return hasAnyRole ?
            <div className={styles.AdminNav}>
                <MDBDropdown>
                    <MDBDropdownToggle caret color="secondary">
                        ADMINISTRATION
                    </MDBDropdownToggle>
                    <MDBDropdownMenu basic>
                        <Link to="/administration/create-content">
                            <MDBDropdownItem>Create
                                content</MDBDropdownItem>
                        </Link>

                        {
                            hasRoleAdmin ?
                                <> <
                                    MDBDropdownItem divider/>
                                    <Link to="/administration/manage-roles">
                                        <MDBDropdownItem>Manage roles</MDBDropdownItem>
                                    </Link>
                                </> : null
                        }
                    </MDBDropdownMenu>
                </MDBDropdown>
            </div> : null;
    }

    return (
        <div className={styles.container}>
            <header className={styles.Nav}>
                {administrationBar()}
                {username()}
                <ul>
                    <li>
                        <Link to="/"> <strong>Home</strong></Link>
                    </li>
                    {user.authorities.length ?
                        <li><Link to="/logout" onClick={logout}><strong>Logout</strong></Link></li>
                        : <>
                            <li><Link to="/login"><strong>Login</strong></Link></li>
                            <li><Link to="/register"><strong>Register</strong></Link></li>
                        </>
                    }
                </ul>
            </header>
        </div>
    )
}

export default Navigation