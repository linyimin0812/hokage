/**
 * @author linyimin
 * @date 2021/3/13 6:28 pm
 * @email linyimin520812@gmail.com
 * @description add account
 */
import React from 'react'
import { Form, Input, Modal, Select, Radio, Button } from 'antd'
import { Option } from '../../axios/action/server/server-type'
import { RadioChangeEvent } from 'antd/lib/radio'
import Dragger from 'antd/lib/upload/Dragger'
import { InboxOutlined } from '@ant-design/icons/lib'

type AddAccountPropsType = {
    onModalOk: (value: any) => void,
    onModalCancel: () => void,
    isModalVisible: boolean
}

type AddAccountStateType = {
    serverOptions: Option[],
    passwordHidden: boolean,
    keyHidden: boolean
}

export default class AddAccount extends React.Component<AddAccountPropsType, AddAccountStateType> {

    state = {
        serverOptions: [{
            label: '10.0.0.1',
            value: '10.0.0.1'
        }],
        passwordHidden: false,
        keyHidden: true
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

    render() {
        const { isModalVisible } = this.props
        const { serverOptions, passwordHidden, keyHidden } = this.state
        return (
            <Modal
                title="添加账号"
                visible={isModalVisible}
                footer={null}
                closable={false}
            >
                <Form
                    name="addAccountForm"
                    onFinish={this.props.onModalOk}
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                >
                    <Form.Item label={'服务器类型'} name="operateType" initialValue={[]} required>
                        <Select placeholder={"请选择"}>
                            <Select.Option value={'ip'}>ip</Select.Option>
                            <Select.Option value={'serverGroup'}>分组</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label={' '} name="operateContent" initialValue={[]} colon={false} required >
                        <Select placeholder={"请选择(支持多选)"} mode={'multiple'}>
                            {serverOptions.map((serverOption, index) => <Select.Option value={serverOption.value} key={index}>{serverOption.label}</Select.Option> )}
                        </Select>
                    </Form.Item>

                    <Form.Item name={'account'} label={<span>账号</span>} required>
                        <Input placeholder="请指定服务器域名" />
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
                        <Dragger style={{textAlign: 'center'}}>
                            <p><InboxOutlined translate = {false} /></p>
                            <p>点击或者拖拽密钥文件上传</p>
                        </Dragger>
                    </Form.Item>

                    <Form.Item wrapperCol={{ span: 24 }}>
                        <div style={{textAlign: 'center'}}>
                            <Button type="primary" htmlType="submit">
                                添加
                            </Button>
                            <Button
                                style={{ margin: '0 8px' }}
                                onClick={() => this.props.onModalCancel()}
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
