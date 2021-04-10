import React from 'react'
import { Avatar, Dropdown, Layout, Menu, message } from 'antd'
import style from './layout.module.css'
import Icon, { FullscreenExitOutlined, FullscreenOutlined } from '@ant-design/icons'
import history from '../libs/history'
import { Models } from '../libs/model'
import { UserAction } from '../axios/action'
import screenfull from 'screenfull'

export interface HeaderPropsType {
    toggle: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void,
    collapsed: boolean
}

export interface HeaderStateType {
    isFullScreen: boolean,
}

export default class Header extends React.Component<HeaderPropsType, HeaderStateType> {

    handleLogout = () => {
        const userInfo = Models.get('userInfo')

        UserAction.logout({email: userInfo.email}).then(value => {
            if (value) {
                // 跳转到登录页
                history.push('/app/login')
                Models.remove('userInfo')
                message.success(`${userInfo.username}已退出`)
                return
            }
        }).catch(err => {
            message.error("退出失败，请稍后重试", 5)
            console.log("Service.register catch: ", JSON.stringify(err))
        })
    }

    menu = () => {
        return (
            <Menu>
                <Menu.Item onClick={this.handleLogout}>
                    <Icon type={'layout'} style={{marginRight: 10}} translate />退出登录
                </Menu.Item>
            </Menu>
        )
    }

    screenFull = () => {
        if (screenfull.isEnabled) {
            screenfull.request().then(() => {
                this.setState({
                    isFullScreen: true
                })
            })
        }
    };
    exitFullScreen = () => {
        const { isFullScreen } = this.state
        if (screenfull.isEnabled && isFullScreen) {
            screenfull.exit().then(() => {
                this.setState({
                    isFullScreen: false
                })
            })
        }
    }

    render() {
        const username = (Models.get('userInfo') ? Models.get('userInfo').username : 'Hokage')
        const { isFullScreen } = this.state
        return (
            <Layout.Header style={{padding: 0}}>
                <div className={style.header}>
                    <div className={style.trigger} onClick={this.props.toggle}>
                        <Icon type={this.props.collapsed ? 'menu-unfold' : 'menu-fold'} translate />
                    </div>
                    <div className={style.right}>
                        <Dropdown overlay={this.menu}>
                            <span className={style.action}>
                                <Avatar size={'small'} style={{marginRight: 8, backgroundColor: '#f56a00', verticalAlign: 'middle'}}>
                                    {username.substr(0, 1)}
                                </Avatar>
                                {username}
                            </span>
                        </Dropdown>
                    </div>
                </div>
                <div className={style.right}>
                    {
                        isFullScreen ? (
                            <FullscreenExitOutlined translate="true" onClick={this.exitFullScreen} style={{paddingRight: '64px', color: 'black'}} />
                        ) : (
                            <FullscreenOutlined translate="true" onClick={this.screenFull} style={{paddingRight: '64px', color: 'black'}} />
                        )
                    }
                </div>
            </Layout.Header>
        )
    }
}
