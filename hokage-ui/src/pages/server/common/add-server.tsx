import React from 'react'
import { Modal, Form, Select, Button, Input, Tooltip, Divider, message, Radio, Spin } from 'antd'
import { QuestionCircleOutlined } from '@ant-design/icons'
import isIP from 'is-ip'
import { PlusOutlined } from '@ant-design/icons/lib'
import { Option, ServerForm, ServerGroupOption } from '../../../axios/action/server/server-type'
import { UserServerOperateForm } from '../../../axios/action/user/user-type'
import { ServerAction } from '../../../axios/action/server/server-action'
import { RadioChangeEvent } from 'antd/lib/radio'
import { emptyFunction, getHokageUid } from '../../../libs'
import { FileUpload } from '../../common/file-upload'
import { observer } from 'mobx-react'
import { FormInstance } from 'antd/es'
import store from '../all-server/store'
type AddServerPropTypes = {
  onModalOk: (value: any) => void,
  onModalCancel: () => void,
  isModalVisible: boolean,
  initValue: ServerForm | undefined
}

type AddServerStateTypes = {
  serverGroupOptions: ServerGroupOption[],
  userOptions: Option[],
  isAddGroup: boolean,
  passwordHidden: boolean,
  keyHidden: boolean,
}
@observer
export default class AddServer extends React.Component<AddServerPropTypes, AddServerStateTypes> {

  state = {
    isAddGroup: false,
    userOptions: [],
    serverGroupOptions: [],
    passwordHidden: false,
    keyHidden: true
  }

  componentDidMount() {
    this.listServerGroupOptions()
  }

  formRef = React.createRef<FormInstance>()

  listServerGroupOptions = () => {
    ServerAction.listServerGroup().then(data => {
      this.setState({serverGroupOptions: data})
    }).catch(err => message.error(err))
  }

  onGroupModalOk = (value: { name: string, description: string }) => {
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      serverGroup: {
        name: value.name,
        description: value.description || '',
      }
    }
    ServerAction.addServerGroup(form).then(result => {
      if (result) {
        message.success(`已添加'${value.name}'分组`)
        this.setState({serverGroupOptions: result, isAddGroup: false})
      } else {
        message.error("添加分组失败")
      }
    }).catch(err => message.error(err))
  }

  loginTypeChange = (e: RadioChangeEvent) => {
    if (!e || !e.target) {
      return
    }
    if (e.target.value === 0) {
      this.setState({ passwordHidden: false, keyHidden: true })
    }
    if (e.target.value === 1) {
      this.setState({ passwordHidden: true, keyHidden: false })
    }
  }

  validateIP = () => ({
    validator(_: any, value: string) {
      if (value === '' || value === undefined || isIP(value)) {
        return Promise.resolve();
      }
      return Promise.reject('IP格式错误,请重新输入');
    },
  })

  validateSshPort = () => ({
    validator(_: any, value: string) {
      if (value === '' || value === undefined || /^\d+$/.test(value)) {
        return Promise.resolve();
      }
      return Promise.reject('端口号只能是数字哦!');
    },
  })

  render() {
    const { isModalVisible, initValue } = this.props
    const { isAddGroup, serverGroupOptions, passwordHidden, keyHidden } = this.state
    if (this.formRef.current) {
      this.formRef.current.setFieldsValue(initValue)
    }
    return (
      <Modal title="添加服务器" visible={isModalVisible} footer={null} closable={false}>
        <Spin spinning={store.isFetching}>
          <Form name="server-add" onFinish={this.props.onModalOk} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }} ref={this.formRef}>
            <Form.Item name="ip" label="服务器IP" hasFeedback rules={[{ required: true, message: '请输入服务器IP' }, this.validateIP,]}>
              <Input placeholder="请输入服务器IP" />
            </Form.Item>

            <Form.Item name={'domain'} label={<span>域名<Tooltip title="指定服务器域名,如:master.pcncad.club."><QuestionCircleOutlined translate="true" /></Tooltip></span>}>
              <Input placeholder="请指定服务器域名" />
            </Form.Item>

            <Form.Item name="sshPort" label="SSH端口" hasFeedback rules={[{ required: true, message: '请输入SSH端口号' }, this.validateSshPort]}>
              <Input placeholder="请输入SSH端口号" />
            </Form.Item>

            <Form.Item
              name="account" hasFeedback
              label={<span>管理账号<Tooltip title="此账号用于系统操作服务器, 请保证此账号具有root权限"><QuestionCircleOutlined translate="true" /></Tooltip></span>}
              rules={[{ required: true, message: '此账号用于系统操作服务器, 请保证此账号具有root权限', }]}
            >
              <Input placeholder="请输入管理账号" />
            </Form.Item>

            <Form.Item name={'loginType'} label={<span>登录类型</span>} required >
              <Radio.Group onChange={this.loginTypeChange} defaultValue={0}>
                <Radio value={0}>密码</Radio>
                <Radio value={1}>密钥</Radio>
              </Radio.Group>
            </Form.Item>
            <Form.Item hidden={passwordHidden} name="passwd" label=" " required>
              <Input.Password placeholder="请输入账号密码" />
            </Form.Item>
            <Form.Item hidden={keyHidden} name="passwd" label=" " required>
              <FileUpload name={'sshKeyFile'} action={'/api/app/file/upload'} prompt={'点击上传密钥文件'} onChange={emptyFunction} />
            </Form.Item>

            <Form.Item name="serverGroupList" label="指定分组" hasFeedback>
              <Select
                mode="multiple"
                style={{ width: '100%' }}
                placeholder={"请选择管分组(支持多选)"}
                dropdownRender={menu => (
                  <div>
                    {menu}
                    <Divider style={{ margin: '1px 0' }} />
                    <div style={{textAlign: "center"}}>
                    <span
                      style={{color:"#5072D1", cursor: "pointer", padding: "8px 0px"}}
                      onClick={() => { this.setState({isAddGroup: true}) }}
                    >
                      <PlusOutlined translate /> 添加分组
                    </span>
                    </div>
                  </div>
                )}
                options={serverGroupOptions}
              />
            </Form.Item>
            <Form.Item name="description" label="描述" hasFeedback>
              <Input.TextArea placeholder={"请输入服务器描述"} />
            </Form.Item>

            <Form.Item wrapperCol={{ span: 24 }}>
              <div style={{textAlign: 'center'}}>
                <Button type="primary" htmlType="submit">添加</Button>
                <Button style={{ margin: '0 8px' }} onClick={() => this.props.onModalCancel()}>取消</Button>
              </div>
            </Form.Item>
          </Form>
        </Spin>
        <Modal title="添加服务器分组" visible={isAddGroup} footer={null}
          onCancel={() => { this.setState({isAddGroup: false}) }}
        >
          <Form name="server-group-add" onFinish={this.onGroupModalOk} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
            <Form.Item
              name="name"
              label="分组名称"
              rules={[
                {
                  required: true,
                  message: '请输入分组名称',
                },
                () => ({
                  validator(_, value) {
                    if (value === '' || value === undefined || /^[0-9a-zA-Z]+/.test(value)) {
                      return Promise.resolve();
                    }
                    return Promise.reject('分组名称只能是字母或者数字哦!');
                  },
                }),
              ]}
              hasFeedback
            >
              <Input placeholder="请输入分组名称" />
            </Form.Item>
            <Form.Item name="description" label="分组描述">
              <Input placeholder="请输入分组描述" />
            </Form.Item>
            <Form.Item wrapperCol={{ span: 24 }}>
              <div style={{textAlign: 'center'}}>
                <Button type="primary" htmlType="submit">添加</Button>
                <Button style={{ margin: '0 8px', }} onClick={() => {this.setState({isAddGroup: false})}}>取消</Button>
              </div>
            </Form.Item>
          </Form>
        </Modal>
      </Modal>
    )
  }
}
