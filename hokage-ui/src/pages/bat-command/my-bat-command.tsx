import React from 'react'
import { Button, Divider, Table } from 'antd'
import EditBatCommand from './edit-bat-command'

interface MyBatCommandStateType {
    isCreate: boolean// 创建新的批量任务
}

export default class MyBatCommand extends React.Component<any, MyBatCommandStateType> {

  state = {
    isCreate: false
  }

  createBatCommand = () => {
    this.setState({ isCreate: true })
  }

  render() {
    const { isCreate } = this.state
    return (
      <div>
        <Button type="primary" onClick={this.createBatCommand}>创建批量任务</Button>
        <Divider style={{margin: "8px 0px"}} />
        <EditBatCommand isVisible={isCreate} isEdit onChange={(value: boolean) => {this.setState({isCreate: value})}} />
        <Table dataSource={[]}>
          <Table.Column title="任务名称" dataIndex="name" />
          <Table.Column title="任务类型" dataIndex="type" />
          {/*定时,周期*/}
          <Table.Column title="执行方式" dataIndex="execType" />
          <Table.Column title="执行时间" dataIndex="execTime" />
          {/*启动,暂停*/}
          <Table.Column title="状态" dataIndex="status" />
          <Table.Column title="操作" dataIndex="action" />
        </Table>
      </div>
    )
  }

}
