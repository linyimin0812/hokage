import React from 'react'
import { Card, Table } from 'antd'
import { connectionInfoData } from './mock-data'

export default class ConnectionInfo extends React.Component {

  render() {
    return (
      <Card title="网络连接信息表">
        <Table dataSource={connectionInfoData} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="本地地址" dataIndex="localAddress" />
          <Table.Column title="连接地址" dataIndex="foreignAddress" />
          <Table.Column title="连接状态" dataIndex="state" />
          <Table.Column title="进程" dataIndex="pid" />
        </Table>
      </Card>

    )
  }

}
