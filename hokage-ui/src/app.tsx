import React, { Component } from 'react'
import DocumentTitle from 'react-document-title'
import { Layout } from 'antd'
import Header from './layout/header'
import Sider from './layout/sider'
import { Router } from './routes/router'

const { Content, Footer } = Layout

type AppProps = {
}

type AppState = {
  title: string,
  collapsed: boolean,
}

export default class App extends Component<AppProps, AppState> {
  state = {
    collapsed: false,
    title: '',
  }

  toggle = () => {
    this.setState({
      collapsed: !this.state.collapsed,
    })
  }
  render() {
    const { title } = this.state;
    return (
      <DocumentTitle title={title}>
        <Layout>
          {/*<SiderCustom collapsed={this.state.collapsed} />*/}
          <Sider collapsed={this.state.collapsed} />
          <Layout className="layout" style={{ flexDirection: 'column' }}>
            <Header toggle={this.toggle} collapsed={this.state.collapsed} />
            <Content style={{ margin: '0 16px', overflow: 'scroll', flex: '1 1 0' }}>
              <Router />
            </Content>
            <Footer id={'ant-layout-footer'} style={{ textAlign: 'center', padding: '0px 0px' }}>
              Server Management ©{new Date().getFullYear()} Created by github@linyimin-bupt
            </Footer>
          </Layout>
        </Layout>
      </DocumentTitle>
    )
  }
}

