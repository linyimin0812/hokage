import React from 'react'
import { Card, Table } from 'antd'
import { diskPartitionData } from './mock-data'

export default class DiskPartition extends React.Component {

  render() {
    return (
      <Card title="磁盘使用率">
        <Table dataSource={diskPartitionData} pagination={false} scroll={{y: 350}} >
          <Table.Column title="NAME" dataIndex="name" />
          {/*渲染进度条*/}
          <Table.Column title="STATUS" dataIndex="status" />
          <Table.Column title="USED%" dataIndex="used" />
          <Table.Column title="MOUNT" dataIndex="mount" />
        </Table>
      </Card>
    )
  }
}
