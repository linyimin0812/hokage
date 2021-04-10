import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import NotFound from './pages/pages/not-found'
import App from './app'
import Login from './pages/login'


// eslint-disable-next-line import/no-anonymous-default-export
export default () => (
    <Router>
        <Switch>
            <Route exact path="/" render={() => <Redirect to="/app/index" push />} />
            <Route exact path="/app/login" component={Login} />
            <Route path="/app/404" component={NotFound} />
            <Route path="/" component={App} />
        </Switch>
    </Router>
);
