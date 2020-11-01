import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import NotFound from './components/pages/not-found'
import App from './App'
import Login from './components/login'


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
