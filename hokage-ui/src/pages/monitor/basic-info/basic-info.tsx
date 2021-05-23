import React from 'react'
import { Card, Table } from 'antd'
import { basicInfoData } from './mock-data'

interface BasicInfoPropsType {
  title: string
}

export default class BasicInfo extends React.Component<BasicInfoPropsType, any> {
  render() {
    return (
      <Card title={this.props.title}>
        <Table dataSource={basicInfoData} pagination={false} showHeader={false} scroll={{y: 350}}>
          <Table.Column dataIndex="name" />
          <Table.Column dataIndex="value" />
        </Table>
      </Card>
    )
  }
}
