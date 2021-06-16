import React from 'react'
import { Card, Table } from 'antd'

export interface LastLogInfoVO {
  account: string,
  port: string,
  from: string,
  latest: string
}

type LoginAccountInfoProp = {
  dataSource: LastLogInfoVO[]
}

export default class LoginAccountInfo extends React.Component<LoginAccountInfoProp> {
  render() {
    const { dataSource } = this.props
    return (
      <Card title="登录账户信息">
        <Table dataSource={dataSource} pagination={false} scroll={{y: 350}}>
          <Table.Column title="账户" dataIndex="account" />
          <Table.Column title="port" dataIndex="port" />
          <Table.Column title="from" dataIndex="from" align={'center'} width={'30%'} />
          <Table.Column title="上次登录时间" dataIndex="latest" align={'center'} width={'30%'} />
        </Table>
      </Card>
    )
  }
}
