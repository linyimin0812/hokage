import React from 'react'
import { Button, Divider, message, Table } from 'antd'
import EditBatCommand from './edit-bat-command'
import { BatCommandOperateForm, BatCommandVO } from '../../axios/action/bat-command/bat-command-type'
import { getHokageUid } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import store from './store'
import { observer } from 'mobx-react'
import { Action } from '../../component/Action'
import { ClockCircleOutlined } from '@ant-design/icons'
import moment from 'moment';

@observer
export default class MyBatCommand extends React.Component {

  componentDidMount() {
    store.searchTaskList()
  }

  onBatCommandEditChange = (value: boolean) => {
    store.form.isModalVisible = value
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
        store.searchTaskList()
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
        store.searchTaskList()
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
        store.searchTaskList()
      } else {
        message.error('上线失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  executeTask = (taskId: number) => {
    store.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskId: taskId
    }
    BatCommandAction.executeTask(form)
      .catch(e => message.error(e)).finally(() => store.loading = false)

  }

  renderAction = (record: BatCommandVO) => {
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskId: record.id
    }
    return (
      <Action>
        <Action.Button
          title={<span>查看</span>}
          action={() => store.editTask(record.id!, false)}
        />
        <Action.Confirm
          title={<span>运行</span>}
          action={() => {this.executeTask(record.id!)}}
          content={`确定运行任务${record.id}?`}
        />
        <Action.Confirm
          title={<span>{record.status === 0 ? '上线' : '下线'}</span>}
          action={() => {record.status === 0 ? this.onlineTask(form) : this.offlineTask(form)}}
          content={`确定下线任务${record.id}?`}
        />
        <Action.Button
          title={<span>修改</span>}
          action={() => store.editTask(record.id!, true)}
        />
        <Action.Confirm
          title={<span>删除</span>}
          action={() => {this.deleteTask(record)}}
          content={`确定删除任务${record.id}?`}
        />
      </Action>
    )
  }

  assembleFormValue = (record: BatCommandVO) => {
    return {
      id: record.id,
      taskName: record.taskName,
      taskType: record.taskType,
      execType: record.execType,
      execTime: moment(record.execTime),
      execServers: record.execServers,
      execCommand: record.execCommand
    }
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
    return (
      <div>
        <Button type="primary" onClick={() => store.createTask()}>创建批量任务</Button>
        <Divider style={{margin: "8px 0px"}} />
        <EditBatCommand />
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
          <Table.Column title="操作" render={this.renderAction} width={'20%'} />
        </Table>
      </div>
    )
  }

}
