import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import NotFound from './layout/not-found'
import App from './app'


// eslint-disable-next-line import/no-anonymous-default-export
export default () => (
    <Router>
        <Switch>
            <Route exact path="/" render={() => <Redirect to="/app/index" push />} />
            <Route path="/" component={App} />
            <Route component={NotFound} />
        </Switch>
    </Router>
)
