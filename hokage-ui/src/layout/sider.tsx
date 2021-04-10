import React, { ReactText } from 'react'
import { menus, MenusType } from './menus'
import { Layout, Menu } from 'antd'
import { MenuInfo, SelectInfo } from 'rc-menu/lib/interface'
import { RouteComponentProps } from 'react-router-dom'
import history from '../libs/history'
import Icon from '@ant-design/icons'
import { hasPermissions } from '../libs'

interface SiderPropsType {
    collapsed: boolean,
    history: RouteComponentProps['history'],
    location: RouteComponentProps['location']
}

type SiderStateType = {
    selectedKeys: string[],
    openKeys: ReactText[]
}

class Sider extends React.Component<SiderPropsType, SiderStateType> {

    constructor(props: SiderPropsType) {
        super(props);
        for (let item of menus) {
            if (!item.child) {
                continue
            }
            for (let child of item.child) {
                this.keyMap[child.path!] = child.title
            }
        }
        this.state = {
            selectedKeys: [],
            openKeys: [this.keyMap[window.location.pathname]]
        }
    }
    private keyMap: {[path: string]: string} = {}

    handleSelect = (info: SelectInfo) => {
        history.push(info.key as string)
    }

    handleClick = (info: MenuInfo) => {
        const key = info.key as string
        if (key === this.state.selectedKeys[0] && key !== this.props.location.pathname) {
            this.props.history.push(key)
        }
    }

    makeMenu = (menu: MenusType) => {
        if (menu.auth !== undefined && !hasPermissions(menu.auth)) return null;
        return (menu.child) ? this.makeSubMenu(menu) : this.makeItem(menu)
    };

    makeItem = (menu: MenusType) => {
        return (
            <Menu.Item key={menu.path}>
                {menu.icon && <Icon translate type={menu.icon} />}
                <span>{menu.title}</span>
            </Menu.Item>
        )
    };

    makeSubMenu = (subMenu: MenusType) => {
        return (
            <Menu.SubMenu key={subMenu.title} title={<span><Icon translate type={subMenu.icon} /><span>{subMenu.title}</span></span>}>
                {subMenu.child!.map(menu => this.makeMenu(menu))}
            </Menu.SubMenu>
        )
    };

    render() {
        return (
            <Layout.Sider collapsed={this.props.collapsed} >
                <div className="">
                    <img src={'logo'} alt="Logo" />
                    <img src={'logoText'} alt="logo-text" style={{marginLeft: 25, width: 70}} />
                </div>
                <Menu
                    theme="dark"
                    mode="inline"
                    selectedKeys={[window.location.pathname]}
                    openKeys={this.state.openKeys as string[]}
                    onSelect={this.handleSelect}
                    onClick={this.handleClick}
                    onOpenChange={ openKeys => this.setState({openKeys})}
                >
                    {menus.map(menu => this.makeMenu(menu))}
                </Menu>
            </Layout.Sider>
        )
    }

}
export default Sider
