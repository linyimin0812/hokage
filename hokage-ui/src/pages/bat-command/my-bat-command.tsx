import React from 'react'
import { Button, Divider, message, Table } from 'antd'
import EditBatCommand from './edit-bat-command'
import { BatCommandOperateForm } from '../../axios/action/bat-command/bat-command-type'
import { getHokageUid } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import store from './store'
import { observer } from 'mobx-react'

interface MyBatCommandStateType {
    isCreate: boolean// 创建新的批量任务
}

@observer
export default class MyBatCommand extends React.Component<any, MyBatCommandStateType> {

  state = {
    isCreate: false
  }

  componentDidMount() {
    this.searchCommand()
  }

  searchCommand = () => {
    store.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid()
    }
    BatCommandAction.search(form).then(commandVOList => {
      store.records = commandVOList ? commandVOList : []
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  createBatCommand = () => {
    this.setState({ isCreate: true })
  }

  onBatCommandEditChange = (value: boolean) => {
    this.setState({isCreate: value})
    this.searchCommand()
  }

  render() {
    const { isCreate } = this.state
    return (
      <div>
        <Button type="primary" onClick={this.createBatCommand}>创建批量任务</Button>
        <Divider style={{margin: "8px 0px"}} />
        <EditBatCommand isVisible={isCreate} isEdit onChange={this.onBatCommandEditChange} />
        <Table dataSource={store.records} loading={store.loading}>
          <Table.Column title="id" dataIndex="id" />
          <Table.Column title="任务名称" dataIndex="taskName" />
          <Table.Column title="任务类型" dataIndex="taskType" />
          {/*定时,周期*/}
          <Table.Column title="执行方式" dataIndex="execType" />
          <Table.Column title="执行时间" dataIndex="execTime" />
          <Table.Column title="执行机器" dataIndex="execServers" />
          {/*未开始, 正在执行, 结束*/}
          <Table.Column title="状态" dataIndex="status" />
          {/*运行一次, 结束, 删除, 修改*/}
          <Table.Column title="操作" dataIndex="action" />
        </Table>
      </div>
    )
  }

}
