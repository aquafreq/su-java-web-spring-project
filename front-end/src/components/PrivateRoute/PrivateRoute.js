import React from 'react';
import {Route, Redirect} from 'react-router-dom';


export const PrivateRoute = ({component: Component, ...rest}) => (
    <Route {...rest}
           render={props => {
               const currentUser = authenticationService.currentUserValue;
               authentication ?
                   <Redirect to={{pathname: '/login', state: {from: props.location}}}/>
                   : <Component {...props} />
           }}/>
)
