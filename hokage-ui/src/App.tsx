import React, { Component } from 'react';
import Routes from './routes';
import DocumentTitle from 'react-document-title';
import SiderCustom from './components/SiderCustom';
import HeaderCustom from './components/HeaderCustom';
import { Layout } from 'antd';
import Login from './components/Login';

const { Content, Footer } = Layout;

type AppProps = {
    auth: any;
    responsive: any;
};

export default class App extends Component<AppProps> {
    state = {
        collapsed: false,
        title: '',
    };
    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };
    render() {
        const { title } = this.state;
        const { auth = { data: {} }, responsive = { data: {} } } = this.props;
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
                                auth === true ? <Routes auth={auth} /> : <Login />
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

