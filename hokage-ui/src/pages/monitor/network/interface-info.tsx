import React from 'react'
import { Card, Table } from 'antd'

const data: any[] = [
  {
    interface: "lo",
    IPAddress: "127.0.0.1"
  },
  {
    interface: "enp4s0",
    IPAddress: "192.168.110.185"
  },
  {
    interface: "docker0",
    IPAddress: "172.17.0.1"
  },
]

export default class InterfaceInfo extends React.Component<any, any>{

  render() {
    return (
      <Card title="网络接口信息">
        <Table dataSource={data} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="接口名称" dataIndex="interface" />
          <Table.Column title="地址" dataIndex="IPAddress" />
        </Table>
      </Card>

    )
  }

}
