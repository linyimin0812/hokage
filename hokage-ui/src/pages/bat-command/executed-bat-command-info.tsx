import React from 'react'
import { Drawer, Form, Tabs } from 'antd'
import Editor from 'react-monaco-editor'
import { TaskInfoVO } from '../../axios/action/bat-command/bat-command-type'

interface ExecutedBatCommandInfoProps {
  isVisible: boolean,
  onCloseDrawer: (value: boolean) => void,
  data: TaskInfoVO
}

// 表单样式
const formItemLayout = {
  labelCol: { xs: { span: 24 }, sm: { span: 4 } },
  wrapperCol: { xs: { span: 24 }, sm: { span: 20 } },
}

export default class ExecutedBatCommandInfo extends React.Component<ExecutedBatCommandInfoProps> {

  render() {
    const { isVisible } = this.props
    const { commandVO, taskResultDetailVO } = this.props.data
    if (!commandVO || !taskResultDetailVO) {
      return null
    }
    return (
      <Drawer
        width={640}
        title="批量命令详情"
        placement="right"
        onClose={() => { this.props.onCloseDrawer(false) }}
        visible={isVisible}
      >
        <Form{...formItemLayout}>
          <Form.Item name="name" label="任务名称">
            <span>{commandVO.taskName}</span>
          </Form.Item>
          <Form.Item name="type" label="任务类型">
            <span>{commandVO.taskType === 0 ? 'shell' : 'unknown'}</span>
          </Form.Item>
          <Form.Item name="execType" label="执行类型">
            <span>{commandVO.execType === null ? 'fixed-date' : 'unknown'}</span>
          </Form.Item>
          <Form.Item name="execTime" label="执行时间">
            <span>{commandVO.execTime}</span>
          </Form.Item>
          <Form.Item name="servers" label="执行机器">
            <span>{taskResultDetailVO.execServer}</span>
          </Form.Item>

          <Form.Item name="startTime" label="开始时间">
            <span>{taskResultDetailVO.startTime}</span>
          </Form.Item>
          <Form.Item name="endTime" label="结束时间">
            <span>{taskResultDetailVO.endTime}</span>
          </Form.Item>

          <Form.Item name="cost" label="执行时长">
            <span>{taskResultDetailVO.cost}</span>
          </Form.Item>
          <Form.Item name="exitCode" label="ExitCode">
            <span>{taskResultDetailVO.exitCode}</span>
          </Form.Item>
        </Form>
        <Tabs type="card">
          <Tabs.TabPane tab="命令脚本" key="bat_command">
            <Editor language="shell" height="300px" options={{readOnly: true}} value={commandVO.execCommand} />
          </Tabs.TabPane>
          <Tabs.TabPane tab="结果输出" key="bat_command_result">
            <Editor language="shell" height="300px" options={{readOnly: true}} value={taskResultDetailVO.execResult} />
          </Tabs.TabPane>
        </Tabs>
      </Drawer>
    )
  }

}
