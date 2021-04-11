import React, { useEffect, useState } from 'react';
import { Route, Switch } from 'react-router-dom'
import ModuleRoutes from '../layout/routes'
import { RouteParam } from '../libs'
import NotFound from '../layout/not-found'

// 组合路由
export const Router = () => {
    const [routes, setRoutes] = useState<RouteParam[]>([])
    useEffect(() => {
        const tempRoutes: RouteParam[] = []
        for (let moduleRoute of ModuleRoutes) {
            for (let route of moduleRoute.route) {
                route.path = moduleRoute.prefix + route.subPath
                tempRoutes.push(route)
            }
        }
        setRoutes(tempRoutes)
    }, [])
    return (
        <Switch>
            {routes.map(route => <Route exact strict path={route.path} key={route.path} component={route.component} /> )}
            <Route component={NotFound} />
        </Switch>
    )

}
