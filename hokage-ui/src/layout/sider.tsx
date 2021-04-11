import React, { ReactText } from 'react'
import { menus, MenusType } from './menus'
import { Layout, Menu } from 'antd'
import { SelectInfo } from 'rc-menu/lib/interface'
import history from '../libs/history'
import style from './layout.module.css'
import { Link } from 'react-router-dom'
import { IconMap } from '../libs/icon-config'
import logo from './logo.png'

interface SiderPropsType {
    collapsed: boolean
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

    makeMenu = (menu: MenusType) => {
        // if (menu.auth !== undefined && !hasPermissions(menu.auth)) return null;
        return (menu.child) ? this.makeSubMenu(menu) : this.makeItem(menu)
    };

    makeItem = (menu: MenusType) => {
        const Icon = IconMap[menu.icon]
        return (
            <Menu.Item key={menu.path}>
                {menu.icon && Icon}
                <Link to={menu.path!}>
                    <span className="nav-text">{menu.title}</span>
                </Link>
            </Menu.Item>
        )
    };

    makeSubMenu = (subMenu: MenusType) => {
        const Icon = IconMap[subMenu.icon]
        return (
            <Menu.SubMenu key={subMenu.title} title={<span>{Icon}<span>{subMenu.title}</span></span>}>
                {subMenu.child!.map(menu => this.makeMenu(menu))}
            </Menu.SubMenu>
        )
    };

    render() {
        return (
            <Layout.Sider collapsed={this.props.collapsed} >
                <div className={style.logo}>
                    <img src={logo} alt="Logo" style={{width: '100%', height: '100%'}} />
                </div>
                <Menu
                    mode="inline"
                    selectedKeys={[window.location.pathname]}
                    openKeys={this.state.openKeys as string[]}
                    onSelect={this.handleSelect}
                    onOpenChange={ openKeys => this.setState({openKeys})}
                >
                    {menus.map(menu => this.makeMenu(menu))}
                </Menu>
            </Layout.Sider>
        )
    }

}
export default Sider
