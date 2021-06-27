import React from 'react'
import { Button, Divider, message, Table } from 'antd'
import EditBatCommand, { FormDataType } from './edit-bat-command';
import { BatCommandOperateForm, BatCommandVO } from '../../axios/action/bat-command/bat-command-type'
import { getHokageUid } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import store from './store'
import { observer } from 'mobx-react'
import { Action } from '../../component/Action'
import moment from 'moment'

interface MyBatCommandStateType {
    isCreate: boolean,// 创建新的批量任务
    initFormValue: FormDataType
}

@observer
export default class MyBatCommand extends React.Component<any, MyBatCommandStateType> {

  state = {
    isCreate: false,
    initFormValue: {} as FormDataType
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

  renderExecServer = (recrod: BatCommandVO) => {
    return <span>{recrod.execServerList?.join(', ')}</span>
  }

  renderAction = (record: BatCommandVO) => {
    const initFormValue: FormDataType = {
      id: record.id,
      taskName: record.taskName,
      taskType: record.taskType,
      execType: record.execType,
      execTime: moment(record.execTime),
      execServers: record.execServers,
      execCommand: record.execCommand,
    }
    return (
      <Action>
        <Action.Confirm
          title={<span>运行</span>}
          action={() => {alert('运行')}}
          content={`确定运行任务${record.id}?`}
        />
        <Action.Confirm
          title={<span>下线</span>}
          action={() => {alert('下线')}}
          content={`确定下线任务${record.id}?`}
        />
        <Action.Button
          title={<span>修改</span>}
          action={() => {this.setState({isCreate: true, initFormValue: initFormValue})}}
        />
        <Action.Confirm
          title={<span>删除</span>}
          action={() => {alert('删除')}}
          content={`确定删除任务${record.id}?`}
        />
      </Action>
    )
  }

  render() {
    const { isCreate, initFormValue } = this.state
    return (
      <div>
        <Button type="primary" onClick={this.createBatCommand}>创建批量任务</Button>
        <Divider style={{margin: "8px 0px"}} />
        <EditBatCommand isVisible={isCreate} isEdit onChange={this.onBatCommandEditChange} initValue={initFormValue} />
        <Table dataSource={store.records} loading={store.loading}>
          <Table.Column title="id" dataIndex="id" />
          <Table.Column title="任务名称" dataIndex="taskName" />
          <Table.Column title="任务类型" dataIndex="taskType" />
          {/*定时,周期*/}
          <Table.Column title="执行方式" dataIndex="execType" />
          <Table.Column title="执行时间" dataIndex="execTime" />
          <Table.Column title="执行机器" render={this.renderExecServer} ellipsis />
          {/*未开始, 正在执行, 结束*/}
          <Table.Column title="状态" dataIndex="status" />
          {/*运行一次, 结束, 删除, 修改*/}
          <Table.Column title="操作" render={this.renderAction} />
        </Table>
      </div>
    )
  }

}
