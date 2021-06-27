import React from 'react'
import { Button, DatePicker, Divider, Form, Input, message, Modal, Select, Spin } from 'antd';
import Editor from 'react-monaco-editor'
import { FormInstance } from 'antd/lib/form'
import { Moment } from 'moment'
import { getHokageUid, range } from '../../libs'
import { FixedDateTaskForm } from '../../axios/action/bat-command/bat-command-type'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import { observer } from 'mobx-react'
import store from './store'
import serverStore from '../server/my-server/store'

// 表单数据类型
export interface FormDataType {
  id?: number,
  taskName: string,
  taskType: number,
  execType: number,
  execTime: Moment,
  execServers: number[],
  execCommand: string
}

interface EditBatCommandPropsType {
  isVisible: boolean, // 显示弹窗
  onChange: Function, // 关闭弹窗
  isEdit: boolean, // 是否编辑
  initValue: FormDataType
}

// 表单样式
const formItemLayout = {
  labelCol: { xs: { span: 24 }, sm: { span: 4 } },
  wrapperCol: { xs: { span: 24 }, sm: { span: 20 } },
}

@observer
export default class EditBatCommand extends React.Component<EditBatCommandPropsType> {

  componentDidMount() {
    serverStore.fetchRecords()
  }

  formRef = React.createRef<FormInstance>()

  onCancel = () => {
    this.formRef.current!.resetFields()
    this.props.onChange(false)
  }

  onFinish = (value: FormDataType) => {
    const { id } = this.props.initValue
    const form: FixedDateTaskForm = {
      id: id ? id : 0,
      operatorId: getHokageUid(),
      taskName: value.taskName,
      taskType: value.taskType,
      execType: value.execType,
      execTime: value.execTime.format('YYYY-MM-DD HH:mm:ss'),
      execServers: value.execServers,
      execCommand: value.execCommand
    }
    store.loading = true
    BatCommandAction.save(form).then(id => {
      message.info("保存成功")
      this.props.onChange(false)
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  disabledDateTime = () => {
    return {
      disabledMinutes: () => range(0, 60).filter((value) => value % 5 !== 0),
    }
  }

  renderServerOptions = () => {
    return serverStore.records.map(serverVO => {
      return <Select.Option key={serverVO.id} value={serverVO.id}>{`${serverVO.account}@${serverVO.ip}`}</Select.Option>
    })
  }

  render() {
    const { isVisible, isEdit } = this.props
    return (
      <Modal title="任务编辑" visible={isVisible} onCancel={this.onCancel} footer={null} width={800}>
        <Spin spinning={store.loading}>
          <Form
            {...formItemLayout}
            onFinish={this.onFinish}
            ref={this.formRef}
            initialValues={this.props.initValue}
          >
            <Form.Item
              name="taskName"
              label="任务名称"
              rules={[{ required: true, message: "任务名称不能为空" }]}
            >
              <Input style={{width: "50%"}} disabled={!isEdit} />
            </Form.Item>
            <Form.Item name="taskType" label="任务类型">
              <Select style={{width: "50%"}} disabled={!isEdit} >
                <Select.Option value={0}>shell</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item name="execType" label="执行类型">
              <Select style={{width: "50%"}} disabled={!isEdit} >
                <Select.Option value={0}>定时</Select.Option>
                <Select.Option value={1} disabled>周期</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item name="execTime" label="执行时间">
              <DatePicker
                format="YYYY-MM-DD HH:mm"
                disabledTime={this.disabledDateTime}
                showTime={{hideDisabledOptions: true}}
              />
            </Form.Item>
            {/*<Form.Item label={'服务器类型'} name="serverType" initialValue={[]} required>*/}
            {/*  <Select placeholder={"请选择"} style={{width: '50%'}}>*/}
            {/*    <Select.Option value={0}>ip</Select.Option>*/}
            {/*    <Select.Option value={1} disabled>分组</Select.Option>*/}
            {/*  </Select>*/}
            {/*</Form.Item>*/}

            <Form.Item name="execServers" label="执行机器">
              <Select
                mode="multiple"
                placeholder={"请选择服务器(支持多选)"}
                disabled={!isEdit}
              >
                { this.renderServerOptions() }
              </Select>
            </Form.Item>

            <Form.Item name="execCommand" label="执行内容">
              <Editor
                language="shell"
                height="200px"
                options={{readOnly: !isEdit}}
              />
            </Form.Item>
            {
              isEdit ? (
                <Form.Item style={{textAlign: "center"}} >
                  <Button type="primary" htmlType="submit">保存</Button>
                  <Divider type="vertical" />
                  <Button type="primary" onClick={this.onCancel}>取消</Button>
                </Form.Item>
              ) : null
            }
          </Form>
        </Spin>
      </Modal>
    )
  }
}
