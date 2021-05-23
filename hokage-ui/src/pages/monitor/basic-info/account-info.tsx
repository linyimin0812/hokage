import React from 'react'
import { Card, Table } from 'antd'
import { accountInfoData } from './mock-data'

export default class AccountInfo extends React.Component {
  render() {
    return (
      <Card title="账户信息">
        <Table dataSource={accountInfoData} pagination={false} scroll={{y: 350}} >
          <Table.Column title="类型" dataIndex="type" />
          <Table.Column title="用户名" dataIndex="username" />
          <Table.Column title="工作目录" dataIndex="homeDir" />
        </Table>
      </Card>
    )
  }
}
