import React from 'react'
import { Card, Table } from 'antd'

export interface ArpInfoVo {
  ip: string,
  hwType: string,
  mac: string,
  interfaceName: string
}

type ArpCacheTableProp = {
  dataSource: ArpInfoVo[]
}

export default class ArpCacheTable extends React.Component<ArpCacheTableProp> {

  render() {
    const { dataSource } = this.props
    return (
      <Card title="ARP缓存表">
        <Table dataSource={dataSource} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="地址" dataIndex="ip" />
          <Table.Column title="Mac地址" dataIndex="mac" />
          <Table.Column title="接口名称" dataIndex="interfaceName" />
        </Table>
      </Card>
    )
  }
}
