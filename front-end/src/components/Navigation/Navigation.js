import React, {useContext} from 'react';
import {Link} from 'react-router-dom';
import styles from './Navigation.module.css'
import {MDBDropdown, MDBDropdownItem, MDBDropdownMenu, MDBDropdownToggle} from "mdbreact";
import UserContext from "../../auth/UserContext";

const Navigation = () => {
    const userContext = useContext(UserContext)
    const hasRole = role => userContext.userRoles.includes(role)

    function username() {
        return userContext.username ?
            <span> <Link
                to={`/user/profile/${userContext.id}`}><strong>Hello, {userContext.username}</strong></Link></span> : null
    }

    function administrationBar() {
        return (hasRole('ROLE_MODERATOR') ||
            hasRole('ROLE_ADMIN') ||
            hasRole('ROLE_ROOT_ADMIN')) ?
            <div className={styles.AdminNav}>
                <MDBDropdown>
                    <MDBDropdownToggle caret color="secondary">
                        ADMINISTRATION
                    </MDBDropdownToggle>
                    <MDBDropdownMenu basic>
                        <Link to="/administration/create-content">
                            <MDBDropdownItem>Create content</MDBDropdownItem>
                        </Link>
                        {
                            hasRole("ROLE_ADMIN") || hasRole("ROLE_ROOT_ADMIN") ?
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
                    {userContext.userRoles.length ?
                        <li><Link to="/logout"><strong>Logout</strong></Link></li>
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