import React from 'react'
import { Card, Table } from 'antd'
import { ramProcessData } from './mock-data'

export default class RamProcess extends React.Component {

  render() {
    return (
      <Card title="内存使用率">
        <Table dataSource={ramProcessData} pagination={false} scroll={{y: 350}} >
          <Table.Column title="PID" dataIndex="pid" />
          <Table.Column title="USER" dataIndex="user" />
          <Table.Column title="MEM%" dataIndex="memory" />
          <Table.Column title="RSS" dataIndex="rss" />
          <Table.Column title="VSZ" dataIndex="vsz" />
          <Table.Column title="CMD" dataIndex="cmd" />
        </Table>
      </Card>
    )
  }
}
