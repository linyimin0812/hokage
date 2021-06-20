import React from 'react'
import { Card, Table } from 'antd'

export interface InterfaceIpInfoVO {
  interfaceName: string,
  ip: string
}

type InterfaceInfoProp = {
  dataSource: InterfaceIpInfoVO[]
}

export default class InterfaceInfo extends React.Component<InterfaceInfoProp> {

  render() {
    const { dataSource } = this.props
    return (
      <Card title="网络接口信息">
        <Table dataSource={dataSource} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="接口名称" dataIndex="interfaceName" />
          <Table.Column title="地址" dataIndex="ip" />
        </Table>
      </Card>

    )
  }

}
