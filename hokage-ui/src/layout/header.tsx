import React from 'react'
import { Avatar, Dropdown, Layout, Menu, message } from 'antd'
import style from './layout.module.css'
import {
  FullscreenExitOutlined,
  FullscreenOutlined, LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from '@ant-design/icons'
import { UserAction } from '../axios/action'
import screenfull from 'screenfull'
import { getHokageUserInfo, removeHokageUserInfo } from '../libs'

export interface HeaderPropsType {
  toggle: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void,
  collapsed: boolean
}

export interface HeaderStateType {
  isFullScreen: boolean,
}

export default class Header extends React.Component<HeaderPropsType, HeaderStateType> {

  state = {
    isFullScreen: false
  }

  handleLogout = () => {
    const userInfo = getHokageUserInfo()

    UserAction.logout({email: userInfo.email}).then(value => {
      if (value) {
        removeHokageUserInfo()
        message.success(`${userInfo.username}已退出`)
        // 跳转到登录页
        window.location.href = '/app/login'
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
        <Menu.Item onClick={this.handleLogout} style={{textAlign: 'center'}}>
          <LogoutOutlined translate />退出登录
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

  renderMenuFold = () => {
    if (this.props.collapsed) {
      return <MenuUnfoldOutlined translate />
    }
    return <MenuFoldOutlined translate />
  }

  render() {
    const username = (getHokageUserInfo() ? getHokageUserInfo().username : 'Hokage')
    const { isFullScreen } = this.state
    return (
      <Layout.Header style={{padding: 0}}>
        <div className={style.header}>
          <div className={style.trigger} onClick={this.props.toggle}>
            {this.renderMenuFold()}
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
          <div className={style.right}>
            {isFullScreen ? <FullscreenExitOutlined translate="true" onClick={this.exitFullScreen} style={{paddingRight: '64px', color: 'black'}} />
              :<FullscreenOutlined translate="true" onClick={this.screenFull} style={{paddingRight: '64px', color: 'black'}} />}
          </div>
        </div>
      </Layout.Header>
    )
  }
}
