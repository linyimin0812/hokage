import React from 'react'
import { Card, Table } from 'antd'

export interface ConnectionInfoVO {
  protocol: string,
  localIp: string,
  foreignIp: string,
  status: string,
  process: string
}

type ConnectionTableProp = {
  dataSource: ConnectionInfoVO[]
}

export default class ConnectionTable extends React.Component<ConnectionTableProp> {

  render() {
    const { dataSource } = this.props
    return (
      <Card title="网络连接信息表">
        <Table dataSource={dataSource} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="协议" dataIndex="protocol" width={'10%'} />
          <Table.Column title="本地地址" dataIndex="localIp" />
          <Table.Column title="连接地址" dataIndex="foreignIp" />
          <Table.Column title="连接状态" dataIndex="status" />
          <Table.Column title="进程" dataIndex="process" />
        </Table>
      </Card>
    )
  }

}
