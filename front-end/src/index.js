import React from 'react';
import ReactDOM from 'react-dom';
import 'mdbreact/dist/css/mdb.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import './auth/interceptor'
import './index.css';
import 'bootstrap-css-only/css/bootstrap.min.css';
import '../node_modules/font-awesome/css/font-awesome.min.css';
import 'mdbreact/dist/css/mdb.css'
import {HashRouter} from "react-router-dom";

ReactDOM.render(
    <HashRouter>
        <App/>
    </HashRouter>,
    document.getElementById('root'))


// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
