import React, { Component } from 'react'
import { Route, Switch } from 'react-router-dom'
import DocumentTitle from 'react-document-title'
import AllComponents from '../components'
import routesConfig, { IFMenu, IFMenuBase } from './config'
import queryString from 'query-string'


export default class CRouter extends Component {
    createRoute = (key: string) => {
        return routesConfig[key].map((r: IFMenu) => {
            const route = (r: IFMenuBase) => {
                const Component = r.component && AllComponents[r.component]
                return (
                    <Route
                        key={r.route || r.key}
                        exact
                        path={r.route || r.key}
                        render={props => {
                            const reg = /\?\S*/g
                            // 匹配?及其以后字符串
                            const queryParams = window.location.hash.match(reg)
                            // 去除?的参数
                            const { params } = props.match;
                            Object.keys(params).forEach(key => {
                                params[key] = params[key] && params[key].replace(reg, '')
                            })
                            props.match.params = { ...params }
                            const merge = {
                                ...props,
                                query: queryParams ? queryString.parse(queryParams[0]) : {},
                            }
                            // 重新包装组件
                            return (
                                <DocumentTitle title={r.title}>
                                    <Component {...merge} />
                                </DocumentTitle>
                            )
                        }}
                    />
                )
            }

            const subRoute = (r: IFMenu): any =>
                r.subs && r.subs.map((subR: IFMenu) => (subR.subs ? subRoute(subR) : route(subR)))

            return r.component ? route(r) : subRoute(r)
        });
    };
    render() {
        return (
            <Switch>
                {Object.keys(routesConfig).map(key => this.createRoute(key))}
            </Switch>
        )
    }
}
