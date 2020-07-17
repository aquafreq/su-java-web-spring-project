import React from 'react';
import {Route, Redirect} from 'react-router-dom';


// export const AuthenticatedRoute = ({component: Component, ...rest}) => (
//     <Route {...rest}
//            render={props => {
//                const currentUser = authenticationService.currentUserValue;
//                authentication ?
//                    <Redirect to={{pathname: '/login', state: {from: props.location}}}/>
//                    : <Component {...props} />
//            }}/>
// )

export default function AuthenticatedRoute({component: C, appProps, ...rest}) {
    return (
        <Route
            {...rest}
            render={props =>
                appProps.isAuthenticated
                    ? <C {...props} {...appProps} />
                    : <Redirect to="/"/>
            }
        />
    );
}