import React from 'react'
import { Modal, Form, Select, Button, Input, Tooltip, Row } from 'antd';
import { QuestionCircleOutlined } from '@ant-design/icons';
import isIP from 'is-ip'
type AddServerPropTypes = {
  onModalOk: (value: any) => void,
  onModalCancel: () => void,
  isModalVisible: boolean
}

// TODO: 获取真实的所有用户
const ordianryUser: any[] = [];
for (let i = 10; i < 36; i++) {
  ordianryUser.push(<Select.Option key={i.toString(36) + i} value={i.toString(36) + i}>{i.toString(36) + i}</Select.Option>);
}

export default class AddServer extends React.Component<AddServerPropTypes, {}> {

  state = {
    isModalVisible: false
  }

  render() {
    const { isModalVisible } = this.props
    return (
      <Modal
        title="添加服务器"
        visible={isModalVisible}
        footer={null}
      >
        <Form
          name="server-add"
          onFinish={this.props.onModalOk}
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 18 }}
        >
          <Form.Item
            label={
              <span>
                二级域名&nbsp;
                <Tooltip title="指定服务器域名, 如: master.pcncad.club, 请填写master. 若不填写,默认使用主机名作为二级域名.">
                  <QuestionCircleOutlined translate="true" />
                </Tooltip>
              </span>
            }
          >
            <Form.Item
              name="domainName"
              noStyle
            >
              <Input style={{ width: '75%' }} placeholder="请指定服务器域名" />
            </Form.Item>
            <span>
              .pcncad.club
            </span>
          </Form.Item>
          <Form.Item
            name="IP"
            label="服务器IP"
            rules={[
              {
                required: true,
                message: '请输入服务器IP',
              },
              () => ({
                validator(_, value) {
                  if (isIP(value)) {
                    return Promise.resolve();
                  }

                  return Promise.reject('IP格式错误,请重新输入');
                },
              }),
            ]}
            hasFeedback
          >
            <Input placeholder="请输入服务器IP" />
          </Form.Item>
          <Form.Item
            name="sshPort"
            label="SSH端口"
            rules={[
              {
                required: true,
                message: '请输入SSH端口号',
              },
              () => ({
                validator(_, value) {
                  if (/^\d+$/.test(value)) {
                    return Promise.resolve();
                  }
                  return Promise.reject('端口号只能是数字哦!');
                },
              }),
            ]}
            hasFeedback
          >
            <Input placeholder="请输入SSH端口号" />
          </Form.Item>

          <Form.Item
            name="account"
            label="管理账号"
            rules={[
              {
                required: true,
                message: '请输入账号,此账号用于系统操作服务器',
              },
              () => ({
                validator(_, value) {
                  if (value === 'root') {
                    return Promise.resolve();
                  }
                  return Promise.reject('请确保输入的账号具有root权限!');
                },
              }),
            ]}
            hasFeedback
          >
            <Input placeholder="请输入账号,此账号用于系统操作服务器" />
          </Form.Item>
          <Form.Item
            name="password"
            label="密码"
            rules={[
              {
                required: true,
                message: '请输入账号密码',
              },
            ]}
            hasFeedback
          >
            <Input.Password placeholder="请输入账号密码" />
          </Form.Item>

          <Form.Item
            name="confirmPasswd"
            label="确认密码"
            dependencies={['password']}
            hasFeedback
            rules={[
              {
                required: true,
                message: '请再次输入密码',
              },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject('密码不一致, 请重新输入');
                },
              }),
            ]}
          >
            <Input.Password placeholder="请再次输入密码" />
          </Form.Item>

          <Form.Item
            name="operator"
            label="指定管理员"
            hasFeedback
          >
            <Select
              mode="multiple"
              style={{ width: '100%' }}
              placeholder={"请选择管理员(支持多选)"}
            >
              {ordianryUser}
            </Select>
          </Form.Item>
          
          <Form.Item wrapperCol={{ span: 24 }}>
            <div style={{textAlign: 'center'}}>
            <Button type="primary" htmlType="submit">
              添加
                </Button>
            <Button
              style={{
                margin: '0 8px',
              }}
              onClick={() => {
                this.props.onModalCancel();
              }}
            >
              取消
                </Button>
                </div>
          </Form.Item>
        </Form>
      </Modal>
    )
  }
}