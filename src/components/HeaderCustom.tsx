/**
 * Created by hao.cheng on 2017/4/13.
 */
import React, { Component } from 'react';
import screenfull from 'screenfull';
import avater from '../style/imgs/b1.jpg';
import SiderCustom from './SiderCustom';
import { Menu, Icon, Layout, Popover } from 'antd';
import { FullscreenOutlined, FullscreenExitOutlined } from '@ant-design/icons'
import { gitOauthToken, gitOauthInfo } from '../axios';
import { queryString } from '../utils';
import { withRouter, RouteComponentProps } from 'react-router-dom';
import { PwaInstaller } from './widget';
import { connectAlita } from 'redux-alita';
const { Header } = Layout;
const SubMenu = Menu.SubMenu;

type HeaderCustomProps = RouteComponentProps<any> & {
    toggle: () => void;
    collapsed: boolean;
    user: any;
    responsive?: any;
    path?: string;
};
type HeaderCustomState = {
    user: any,
    visible: boolean,
    isFullScreen: boolean,
};

class HeaderCustom extends Component<HeaderCustomProps, HeaderCustomState> {
    state = {
        user: '',
        visible: false,
        isFullScreen: false
    };
    componentDidMount() {
        const QueryString = queryString() as any;
        let _user,
            storageUser = localStorage.getItem('user');

        _user = (storageUser && JSON.parse(storageUser)) || '测试';
        if (!_user && QueryString.hasOwnProperty('code')) {
            gitOauthToken(QueryString.code).then((res: any) => {
                gitOauthInfo(res.access_token).then((info: any) => {
                    this.setState({
                        user: info,
                    });
                    localStorage.setItem('user', JSON.stringify(info));
                });
            });
        } else {
            this.setState({
                user: _user,
            });
        }
    }
    screenFull = () => {
        if (screenfull.isEnabled) {
            screenfull.request();
            this.setState({
              isFullScreen: true
            })
        }
    };
    exitFullScreen = () => {
      const { isFullScreen } = this.state
      if (screenfull.isEnabled && isFullScreen) {
        screenfull.exit()
        this.setState({
          isFullScreen: false
        })
      }
    }
    menuClick = (e: { key: string }) => {
        e.key === 'logout' && this.logout();
    };
    logout = () => {
        localStorage.removeItem('user');
        this.props.history.push('/login');
    };
    popoverHide = () => {
        this.setState({
            visible: false,
        });
    };
    handleVisibleChange = (visible: boolean) => {
        this.setState({ visible });
    };
    render() {
        const { responsive = { data: {} } } = this.props;
        const { isFullScreen } = this.state
        return (
            <Header className="custom-theme header">
                {responsive.data.isMobile ? (
                    <Popover
                        content={<SiderCustom popoverHide={this.popoverHide} />}
                        trigger="click"
                        placement="bottomLeft"
                        visible={this.state.visible}
                        onVisibleChange={this.handleVisibleChange}
                    >
                        <Icon type="bars" className="header__trigger custom-trigger" />
                    </Popover>
                ) : (
                    <Icon
                        className="header__trigger custom-trigger"
                        type={this.props.collapsed ? 'menu-unfold' : 'menu-fold'}
                        onClick={this.props.toggle}
                    />
                )}
                <Menu
                    mode="horizontal"
                    style={{ lineHeight: '64px', float: 'right' }}
                    onClick={this.menuClick}
                >
                    <Menu.Item key="pwa">
                        <PwaInstaller />
                    </Menu.Item>
                    {
                      isFullScreen ? (
                        <FullscreenExitOutlined translate onClick={this.exitFullScreen} />
                      ) : (
                        <FullscreenOutlined translate onClick={this.screenFull} />
                      )
                    }
                    <SubMenu
                        title={
                            <span className="avatar">
                                <img src={avater} alt="头像" />
                            </span>
                        }
                    >
                      <Menu.Item key="logout" style={{textAlign: "center"}}>
                                <span onClick={this.logout}>退出登录</span>
                      </Menu.Item>
                    </SubMenu>
                </Menu>
            </Header>
        );
    }
}

// 重新设置连接之后组件的关联类型
const HeaderCustomConnect: React.ComponentClass<
    HeaderCustomProps,
    HeaderCustomState
> = connectAlita(['responsive'])(HeaderCustom);

export default withRouter(HeaderCustomConnect);
