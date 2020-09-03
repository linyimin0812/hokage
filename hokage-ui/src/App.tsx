import React, { Component } from 'react';
import Routes from './routes';
import DocumentTitle from 'react-document-title';
import SiderCustom from './components/SiderCustom';
import HeaderCustom from './components/HeaderCustom';
import { Layout } from 'antd';
import Login from './components/Login';

const { Content, Footer } = Layout;

type AppProps = {
    responsive: any;
};

type AppState = {
    auth: {
        login: boolean, // 是否登录
        role: number, // 用户角色
        data: {}
    }
    title: string,
    collapsed: boolean,
}

export default class App extends Component<AppProps, AppState> {
    state = {
        collapsed: false,
        title: '',
        auth: {
            login: true,
            role: 2,
            data: {
                name: 'banzhe'
            }
        }
    };

    componentDidMount() {
        console.log('teta');
    }
    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };
    render() {
        const { title } = this.state;
        const { responsive = { data: {} } } = this.props;
        const { auth } = this.state;
        return (
            <DocumentTitle title={title}>
                <Layout>
                    {!responsive.data.isMobile && (
                        <SiderCustom collapsed={this.state.collapsed} />
                    )}
                    <Layout style={{ flexDirection: 'column' }}>
                        <HeaderCustom
                            toggle={this.toggle}
                            collapsed={this.state.collapsed}
                            user={auth.data || {}}
                        />
                        <Content style={{ margin: '0 16px', overflow: 'initial', flex: '1 1 0' }}>
                            {
                                auth.login === true ? <Routes auth={auth} /> : <Login />
                            }
                        </Content>
                        <Footer style={{ textAlign: 'center' }}>
                            server Management ©{new Date().getFullYear()} Created by github@linyimin-bupt
                        </Footer>
                    </Layout>
                </Layout>
            </DocumentTitle>
        );
    }
}

