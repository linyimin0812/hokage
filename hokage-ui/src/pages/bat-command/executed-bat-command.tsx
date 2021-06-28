import React from 'react'
import { Table, Tag } from 'antd'
import EditBatCommand from './edit-bat-command'
import ExecutedBatCommandInfo from './executed-bat-commandInfo'
import store from './store'
import { observer } from 'mobx-react'
import { TaskResultDetailVO, TaskResultVO } from '../../axios/action/bat-command/bat-command-type'

interface ExecutedBatCommandStateType {
  isModalVisible: boolean, // 弹窗显示任务信息
  isDrawerVisible: boolean, // 单机任务详细信息
}

const status2color = {
  success: 'green',
  failed: 'red',
  processing: 'blue'
}

@observer
export default class ExecutedBatCommand extends React.Component<any, ExecutedBatCommandStateType> {

  state = {
    isModalVisible: false,
    isDrawerVisible: false
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
    }
    if (Object.keys(status2color).includes(status)) {
      //@ts-ignore
      return <Tag color={status2color[status]}>{status}</Tag>
    }
    return <Tag color={status2color.processing}>unknown</Tag>
  }

  renderAction = (_: string, record: TaskResultVO) => {
    return <span onClick={() => { this.viewTask(record.taskId) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
  }

  renderNestedTableAction = (record: TaskResultDetailVO) => {
    return <span onClick={() => { this.viewDetailTask(record.taskId) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
  }

  viewTask = (id: number) => {
    store.searchTask(id)
    this.setState({ isModalVisible: true })
  }

  viewDetailTask = (id: string | number) => {
    // TODO: 获取每个机器上的具体的任务详情
    this.setState({ isDrawerVisible: true })
  }

  expandedRowRender = (record: TaskResultVO) => {
    return (
      <Table pagination={false} dataSource={record.resultDetailVOList}>
        <Table.Column title="执行服务器" dataIndex="serverIp" key="server" />
        <Table.Column title="开始时间" dataIndex="startTime" key="startTime" />
        <Table.Column title="结束时间" dataIndex="endTime" key="endTime" />
        <Table.Column title="执行时长" dataIndex="cost" key="cost" />
        <Table.Column title="执行状态" dataIndex="taskStatus" render={this.renderTaskStatus} key="status" />
        <Table.Column title="操作" dataIndex="id" render={this.renderNestedTableAction} key="id" />
      </Table>
    )
  }

  onCloseDrawer = () => {
    this.setState({ isDrawerVisible: false })
  }

  // TODO: 根据任务id获取任务的执行信息

  render() {
    const { isModalVisible, isDrawerVisible } = this.state
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
          <Table.Column title="ExitCode" dataIndex="exitCode" />
          <Table.Column title="操作" dataIndex="id" render={this.renderAction} />
        </Table>
        <EditBatCommand initValue={store.initCommandFomValue} isEdit={false} isVisible={isModalVisible} onChange={(value: boolean) => {this.setState({isModalVisible: value})}} />
        <ExecutedBatCommandInfo isVisible={isDrawerVisible} onCloseDrawer={(value: boolean) => {this.setState({isDrawerVisible: value})}} />
      </div>
    );
  }

}
