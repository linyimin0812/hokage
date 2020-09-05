import React from 'react'
import { HashRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import NotFound from './components/pages/NotFound'
import App from './App'
import Login from './components/login'


export default () => (
    <Router>
        <Switch>
            <Route exact path="/" render={() => <Redirect to="/app/index" push />} />
            <Route path="/app" component={App} />
            <Route path="/404" component={NotFound} />
            <Route path="/login" component={Login} />
        </Switch>
    </Router>
);
