/**
 * Created by hao.cheng on 2017/4/13.
 */
import React, { Component } from 'react';
import screenfull from 'screenfull';
import { Menu, Icon, Layout, Avatar, Row, Col, Carousel } from 'antd';
import { 
  FullscreenOutlined,
  FullscreenExitOutlined,
} from '@ant-design/icons'
import { gitOauthToken, gitOauthInfo } from '../axios';
import { queryString } from '../utils';
import { withRouter, RouteComponentProps } from 'react-router-dom';
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
        const { isFullScreen } = this.state
        return (
            <Header className="custom-theme header">
              <Row>
                <Col span={1}>
                  <Icon 
                    className="header_trigger custom-trigger"
                    type={this.props.collapsed ? 'menu-unfold' : 'menu-fold'}
                    onClick={this.props.toggle} 
                    style={{color: 'white'}}
                  />
                </Col>
              
                <Col span={17} style={{justifyContent: 'center'}}>
                  <Carousel dots={false} autoplay autoplaySpeed={10 * 1000} speed={3000}>
                    <span>
                      服务器node1.pcncad.com的IP发生了变化,由原来的`10.108.210.194`变成了`10.108.211.136`, 由于域名存在缓存,可能会短暂不可用.
                    </span>
                    <span>
                      斑蛰向你申请服务器node1.pcncad.com的使用权限,请及时处理.
                    </span>
                  </Carousel>
                </Col>
                <Col span={6}>
                <Menu
                    mode="horizontal"
                    style={{ lineHeight: '64px', float: 'right' }}
                >
                    {
                      isFullScreen ? (
                        <FullscreenExitOutlined translate='true' onClick={this.exitFullScreen} style={{paddingRight: '64px'}} />
                      ) : (
                        <FullscreenOutlined translate='true' onClick={this.screenFull} style={{paddingRight: '64px'}}/>
                      )
                    }
                    <SubMenu
                        title={
                          <Avatar
                            style={{
                              backgroundColor: "#f56a00",
                              verticalAlign: 'middle'
                            }}
                            size="large"
                          >
                            {"banzhe"}
                          </Avatar>
                        }
                        style={{
                          paddingLeft: '40px', 
                          paddingRight: '40px',
                        }}
                        
                    >
                      <Menu.Item 
                        key="logout" 
                        style={{
                          position: 'absolute',
                          backgroundColor: '#FFFFFF',
                        }}>
                        <span onClick={this.logout}>退出登录</span>
                      </Menu.Item>
                  </SubMenu>
                </Menu>
                </Col>
                </Row>
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
