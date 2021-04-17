import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import App from './app'
import Login from './pages/login'


// eslint-disable-next-line import/no-anonymous-default-export
export default () => (
  <Router>
    <Switch>
      <Route exact path="/" render={() => <Redirect to="/app/index" push />} />
      <Route path="/app/login" component={Login} />
      <Route path="/login" component={Login} />
      <Route path="/" component={App} />
    </Switch>
  </Router>
)
