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
import { ClockCircleOutlined } from '@ant-design/icons';

interface MyBatCommandStateType {
    editCommandVisible: boolean,// 创建新的批量任务
    initFormValue: FormDataType,
    isEdit: boolean
}

@observer
export default class MyBatCommand extends React.Component<any, MyBatCommandStateType> {

  state = {
    editCommandVisible: false,
    initFormValue: {} as FormDataType,
    isEdit: false,
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
    this.setState({ editCommandVisible: true })
  }

  onBatCommandEditChange = (value: boolean) => {
    this.setState({editCommandVisible: value})
    this.searchCommand()
  }

  renderExecServer = (recrod: BatCommandVO) => {
    return <span>{recrod.execServerList?.join(', ')}</span>
  }

  deleteTask = (record: BatCommandVO) => {
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskId: record.id
    }
    store.loading = true
    BatCommandAction.delete(form).then(result => {
      if (result) {
        message.info('删除成功')
        this.searchCommand()
      } else {
        message.error('删除失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  offlineTask = (form: BatCommandOperateForm) => {
    store.loading = true
    BatCommandAction.offline(form).then(result => {
      if (result) {
        message.info('下线成功')
        this.searchCommand()
      } else {
        message.error('下线失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  onlineTask = (form: BatCommandOperateForm) => {
    store.loading = true
    BatCommandAction.online(form).then(result => {
      if (result) {
        message.info('上线成功')
        this.searchCommand()
      } else {
        message.error('上线失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
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
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskId: record.id
    }
    return (
      <Action>
        <Action.Button
          title={<span>查看</span>}
          action={() => {this.setState({editCommandVisible: true, isEdit: false, initFormValue: initFormValue})}}
        />
        <Action.Confirm
          title={<span>运行</span>}
          action={() => {alert('运行')}}
          content={`确定运行任务${record.id}?`}
        />
        <Action.Confirm
          title={<span>{record.status === 0 ? '上线' : '下线'}</span>}
          action={() => {record.status === 0 ? this.onlineTask(form) : this.offlineTask(form)}}
          content={`确定下线任务${record.id}?`}
        />
        <Action.Button
          title={<span>修改</span>}
          action={() => {this.setState({editCommandVisible: true, isEdit: true, initFormValue: initFormValue})}}
        />
        <Action.Confirm
          title={<span>删除</span>}
          action={() => {this.deleteTask(record)}}
          content={`确定删除任务${record.id}?`}
        />
      </Action>
    )
  }

  renderTaskType = (taskType: number) => {
    if (taskType === 0) {
      return <span>shell</span>
    } else {
      return <span>unsupported</span>
    }
  }

  renderExecType = (execType: number) => {
    if (execType === 0) {
      return <span><ClockCircleOutlined translate />{` fixed date`}</span>
    } else {
      return <span>cron</span>
    }
  }

  renderStatus = (status: number) => {
    if (status === 0) {
      return <span style={{color: 'red'}}>下线</span>
    } else if (status === 1) {
      return <span style={{color: 'green'}}>上线</span>
    } else {
      return <span style={{color: 'red'}}>过期</span>
    }
  }

  render() {
    const { editCommandVisible, isEdit, initFormValue } = this.state
    return (
      <div>
        <Button type="primary" onClick={this.createBatCommand}>创建批量任务</Button>
        <Divider style={{margin: "8px 0px"}} />
        <EditBatCommand isVisible={editCommandVisible} isEdit={isEdit} onChange={this.onBatCommandEditChange} initValue={initFormValue} />
        <Table dataSource={store.records} loading={store.loading}>
          <Table.Column title="id" dataIndex="id" />
          <Table.Column title="任务名称" dataIndex="taskName" />
          <Table.Column title="任务类型" dataIndex="taskType" render={this.renderTaskType} />
          {/*定时,周期*/}
          <Table.Column title="执行方式" dataIndex="execType" render={this.renderExecType} />
          <Table.Column title="执行时间" dataIndex="execTime" />
          <Table.Column title="执行机器" render={this.renderExecServer} ellipsis />
          {/*未开始, 正在执行, 结束*/}
          <Table.Column title="状态" dataIndex="status" render={this.renderStatus} />
          {/*运行一次, 结束, 删除, 修改*/}
          <Table.Column title="操作" render={this.renderAction} />
        </Table>
      </div>
    )
  }

}
