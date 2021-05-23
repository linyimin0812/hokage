import React from 'react'
import { Card, Table } from 'antd'
import { arpcCacheTableData } from './mock-data'

export default class ARPCacheTable extends React.Component {

  render() {
    return (
      <Card title="ARP缓存表">
        <Table dataSource={arpcCacheTableData} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="地址" dataIndex="IPAddress" />
          <Table.Column title="硬件类型" dataIndex="hwType" />
          <Table.Column title="Mac地址" dataIndex="macAddress" />
          <Table.Column title="接口名称" dataIndex="interface" />
        </Table>
      </Card>
    )
  }
}
