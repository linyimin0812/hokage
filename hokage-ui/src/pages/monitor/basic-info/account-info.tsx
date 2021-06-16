import React from 'react'
import { Card, Table } from 'antd'

export interface AccountInfoVO {
  type: string,
  account: string,
  home: string,
  description: string
}

type AccountInfoProp = {
  dataSource: AccountInfoVO[]
}

export default class AccountInfo extends React.Component<AccountInfoProp> {
  render() {
    const { dataSource } = this.props
    return (
      <Card title="账户信息">
        <Table dataSource={dataSource} pagination={false} scroll={{y: 350}} >
          <Table.Column title="类型" dataIndex="type" />
          <Table.Column title="用户名" dataIndex="account" />
          <Table.Column title="工作目录" dataIndex="home" ellipsis />
          <Table.Column title="描述" dataIndex="description" ellipsis />
        </Table>
      </Card>
    )
  }
}
