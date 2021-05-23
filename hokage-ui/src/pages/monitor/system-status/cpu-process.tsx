import React from 'react'
import { Card, Table } from 'antd'
import { cpuProcessData } from './mock-data'

export default class CpuProcess extends React.Component {

  render() {
    return (
      <Card title="CPU使用率">
        <Table dataSource={cpuProcessData} pagination={false} scroll={{y: 350}} >
          <Table.Column title="PID" dataIndex="pid" />
          <Table.Column title="USER" dataIndex="user" />
          <Table.Column title="CPU%" dataIndex="cpu" />
          <Table.Column title="RSS" dataIndex="rss" />
          <Table.Column title="VSZ" dataIndex="vsz" />
          <Table.Column title="CMD" dataIndex="cmd" />
        </Table>
      </Card>
    )
  }
}
