import React from 'react';
import {Link} from 'react-router-dom';
import './Navigation.css'

const Navigation = ({isLogged}) => (
    <div className="nav">
        <Link to="/" > <strong>Home</strong></Link>
        {isLogged ?
            <Link to="/logout"><strong>Logout</strong></Link>
            : <>
                <Link to="/login"><strong>Login</strong></Link>
                <Link to="/register"><strong>Register</strong></Link>
            </>
        }
    </div>
);

export default Navigation;