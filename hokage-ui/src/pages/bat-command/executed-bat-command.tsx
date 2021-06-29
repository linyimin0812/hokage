import React from 'react'
import { message, Table, Tag } from 'antd';
import EditBatCommand from './edit-bat-command'
import ExecutedBatCommandInfo from './executed-bat-command-info'
import store from './store'
import { observer } from 'mobx-react'
import {
  BatCommandOperateForm,
  TaskInfoVO,
  TaskResultDetailVO,
  TaskResultVO,
} from '../../axios/action/bat-command/bat-command-type'
import { getHokageUid, status2Color } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'

interface ExecutedBatCommandStateType {
  isModalVisible: boolean, // 弹窗显示任务信息
  isDrawerVisible: boolean, // 单机任务详细信息
  data: TaskInfoVO
}

@observer
export default class ExecutedBatCommand extends React.Component<any, ExecutedBatCommandStateType> {

  state = {
    isModalVisible: false,
    isDrawerVisible: false,
    data: {} as TaskInfoVO
  }

  componentDidMount() {
    store.searchTaskResult()
  }

  renderTaskStatus = (_: string, record: TaskResultVO) => {
    let status = ''
    if (record.taskStatus === 0) {
      status = 'processing'
    } else if (record.taskStatus === 1) {
      status = 'success'
    } else if (record.taskStatus === -1) {
      status = 'failed'
    } else {
      status = 'unknown'
    }
    return <Tag color={status2Color(record.taskStatus)}>{status}</Tag>
  }

  renderAction = (_: string, record: TaskResultVO) => {
    return <span onClick={() => { this.viewTask(record.taskId) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
  }

  renderNestedTableAction = (record: TaskResultDetailVO) => {
    return <span onClick={() => { this.viewDetailTask(record.id) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
  }

  viewTask = (id: number) => {
    store.editTask(id, false)
    this.setState({ isModalVisible: true })
  }

  viewDetailTask = (id: number) => {
    store.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskResultId: id
    }
    BatCommandAction.viewSingleTaskDetail(form).then(taskInfo => {
      if (taskInfo) {
        this.setState({ isDrawerVisible: true, data: taskInfo })
      } else {
        message.error('获取任务详情失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  expandedRowRender = (record: TaskResultVO) => {
    return (
      <Table pagination={false} dataSource={record.resultDetailVOList}>
        <Table.Column title="执行服务器" dataIndex="execServer" key="server" />
        <Table.Column title="开始时间" dataIndex="startTime" key="startTime" />
        <Table.Column title="结束时间" dataIndex="endTime" key="endTime" />
        <Table.Column title="执行时长" dataIndex="cost" key="cost" />
        <Table.Column title="执行状态" dataIndex="taskStatus" render={this.renderTaskStatus} key="status" />
        <Table.Column title="操作" render={this.renderNestedTableAction} key="action" />
      </Table>
    )
  }

  onCloseDrawer = (value: boolean) => {
    this.setState({ isDrawerVisible: value })
  }

  // TODO: 根据任务id获取任务的执行信息

  render() {
    const { isDrawerVisible, data } = this.state
    return (
      <div>
        {/*可扩展表,子表显示机器, 可参考阿里云远程命令的格式*/}
        <Table dataSource={store.taskResultRecords} expandedRowRender={this.expandedRowRender} rowKey={'batchId'} loading={store.loading}>
          <Table.Column title="任务ID" dataIndex="taskId" />
          <Table.Column title="任务名称" dataIndex="taskName" />
          <Table.Column title="执行状态" dataIndex="taskStatus" render={this.renderTaskStatus} />
          <Table.Column title="触发方式" dataIndex="triggerType" />
          <Table.Column title="开始时间" dataIndex="startTime" />
          <Table.Column title="结束时间" dataIndex="endTime" />
          <Table.Column title="执行时长" dataIndex="cost" />
          <Table.Column title="操作" dataIndex="id" render={this.renderAction} />
        </Table>
        <EditBatCommand />
        <ExecutedBatCommandInfo isVisible={isDrawerVisible} onCloseDrawer={this.onCloseDrawer} data={data} />
      </div>
    );
  }

}
