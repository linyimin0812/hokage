import React, { useEffect, useState } from 'react';
import { Route, Switch } from 'react-router-dom'
import ModuleRoutes from '../layout/routes'
import { RouteParam } from '../libs'
import NotFound from '../layout/not-found'
import DocumentTitle from 'react-document-title';
import { menus, MenusType } from '../layout/menus';

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

    const findTitle = (path: string, menus: MenusType[]): string => {
        const menuList = menus.flatMap(menu => menu.child ? [menu].concat(menu.child) : [menu])
        const menu = menuList.find(menu => menu.path === path)
        if (menu) {
            return menu.title
        }
        return 'Hokage'
    }

    const renderComponent = (route: RouteParam) => {
        const Component = route.component
        return (
            <DocumentTitle title={findTitle(route.path!, menus)}>
                <Component />
            </DocumentTitle>
        )
    }

    return (
        <Switch>
            {routes.map(route => <Route exact strict path={route.path} key={route.path} render={() => {return renderComponent(route)}} />)}
            <Route component={NotFound} />
        </Switch>
    )

}
