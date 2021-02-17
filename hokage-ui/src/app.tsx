import React, { Component } from 'react'
import Routes from './routes'
import DocumentTitle from 'react-document-title'
import SiderCustom from './components/sider-custom'
import HeaderCustom from './components/header-custom'
import { Layout } from 'antd'

const { Content, Footer } = Layout

type AppProps = {
}

type AppState = {
    auth: {
        login: boolean, // loggin status
        role: number, // user role
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
            login: false,
            role: 2,
            data: {
                name: 'banzhe'
            }
        }
    };

    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };
    render() {
        const { title } = this.state;
        const { auth } = this.state;
        return (
            <DocumentTitle title={title}>
                <Layout>
                    <SiderCustom collapsed={this.state.collapsed} />
                    <Layout className="layout" style={{ flexDirection: 'column' }}>
                        <HeaderCustom
                            toggle={this.toggle}
                            collapsed={this.state.collapsed}
                            user={auth.data || {}}
                        />
                        <Content style={{ margin: '0 16px', overflow: 'initial', flex: '1 1 0' }}>
                            <Routes />
                        </Content>
                        <Footer style={{ textAlign: 'center' }}>
                            Server Management ©{new Date().getFullYear()} Created by github@linyimin-bupt
                        </Footer>
                    </Layout>
                </Layout>
            </DocumentTitle>
        );
    }
}

