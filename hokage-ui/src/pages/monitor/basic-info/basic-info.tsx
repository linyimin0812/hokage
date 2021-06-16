import React from 'react'
import { Card, Table } from 'antd'

export interface GeneralInfoVO {
  name: string,
  value: string
}

interface BasicInfoPropsType {
  title: string,
  dataSource: GeneralInfoVO[]
}

export default class BasicInfo extends React.Component<BasicInfoPropsType> {
  render() {
    const { dataSource } = this.props
    return (
      <Card title={this.props.title}>
        <Table dataSource={dataSource} pagination={false} showHeader={false} scroll={{y: 350}}>
          <Table.Column dataIndex="name" />
          <Table.Column dataIndex="value" />
        </Table>
      </Card>
    )
  }
}
