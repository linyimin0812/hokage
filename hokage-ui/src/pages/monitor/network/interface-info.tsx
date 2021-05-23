import React from 'react'
import { Card, Table } from 'antd'
import { interfaceInfoData } from './mock-data'

export default class InterfaceInfo extends React.Component {

  render() {
    return (
      <Card title="网络接口信息">
        <Table dataSource={interfaceInfoData} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="接口名称" dataIndex="interface" />
          <Table.Column title="地址" dataIndex="IPAddress" />
        </Table>
      </Card>

    )
  }

}
