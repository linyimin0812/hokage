import React from 'react'
import { Modal, Form, Select, Button, Input } from 'antd'
import { Option } from '../../../axios/action/server/server-type'

type ApplyPropTypes = {
  onModalOk: (value: any) => void,
  onModalCancel: () => void,
  isModalVisible: boolean
}

type ApplyStateTypes = {
  serverOptions: Option[]
}

export default class ApplyServer extends React.Component<ApplyPropTypes, ApplyStateTypes> {

  state = {
    serverOptions: [] as Option[]
  }

  componentDidMount() {
    this.setState({serverOptions: []})
  }

  render() {
    const { isModalVisible } = this.props
    const { serverOptions } = this.state
    return (
      <Modal title="批量申请服务器" visible={isModalVisible} footer={null} closable={false}>
        <Form
          name="applyServerForm"
          onFinish={this.props.onModalOk}
          labelCol={{ span: 4 }}
          wrapperCol={{ span: 18 }}
        >
          <Form.Item label={'操作权限'} name="operatePermission" initialValue={[]} required>
            <Select placeholder={"请选择"}>
              <Select.Option value={'addAccount'}>添加账号</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item label={'权限期限'} name="permissionDuration" initialValue={[]} required>
            <Select placeholder={"请选择"}>
              <Select.Option value={'aDay'}>一天</Select.Option>
              <Select.Option value={'aWeek'}>一周</Select.Option>
              <Select.Option value={'aMonth'}>一月</Select.Option>
              <Select.Option value={'sixMonth'}>六月</Select.Option>
              <Select.Option value={'aYear'}>一年</Select.Option>
              <Select.Option value={'longTerm'}>长期</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item label={'申请内容'} name="operateType" initialValue={[]} required>
            <Select placeholder={"请选择"}>
              <Select.Option value={'ip'}>ip</Select.Option>
              <Select.Option value={'serverGroup'}>分组</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item label={' '} name="operateContent" initialValue={[]} colon={false} required >
            <Select placeholder={"请选择(支持多选)"} mode={'multiple'}>
              {serverOptions.map((serverOption, index) => {
                return <Select.Option value={serverOption.value} key={index}>{serverOption.label}</Select.Option>
              })}
            </Select>
          </Form.Item>
          <Form.Item label={'申请理由'} name="reason" initialValue={[]} required >
            <Input.TextArea placeholder={'请输入申请理由'} />
          </Form.Item>
          <Form.Item wrapperCol={{ span: 24 }}>
            <div style={{textAlign: 'center'}}>
              <Button type="primary" htmlType="submit">申请</Button>
              <Button style={{ margin: '0 8px' }} onClick={this.props.onModalCancel}>取消</Button>
            </div>
          </Form.Item>
        </Form>
      </Modal>
    )
  }
}
