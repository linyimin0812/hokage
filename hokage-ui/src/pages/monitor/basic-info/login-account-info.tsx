import React from 'react'
import { Card, Table } from 'antd'
import { loginAccountInfoData } from './mock-data'

export default class LoginAccountInfo extends React.Component<any, any> {
  render() {
    return (
      <Card title="登录账户信息">
        <Table dataSource={loginAccountInfoData} pagination={false} scroll={{y: 350}}>
          <Table.Column title="类型" dataIndex="type" />
          <Table.Column title="用户名" dataIndex="username" />
          <Table.Column title="登录时间" dataIndex="loginTime" />
        </Table>
      </Card>
    )
  }
}
