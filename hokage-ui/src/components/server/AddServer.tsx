import React from 'react'
import { Modal, Form, Select, Button, Input, Tooltip, Divider, message } from 'antd';
import { QuestionCircleOutlined } from '@ant-design/icons'
import isIP from 'is-ip'
import { PlusOutlined } from '@ant-design/icons/lib';
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
        isModalVisible: false,
        isAddGroup: false,
        data: ordianryUser
    }

    onGroupModalOk = (value: any) => {
        console.log(value)
        this.setState({
            isAddGroup: false,
            data: this.state.data.concat(<Select.Option key={value.groupName} value={value.groupName}>{`${value.groupName}(${value.groupDes})`}</Select.Option>)
        })
        message.loading({ content: 'Loading...', key: 'addUser' });
        setTimeout(() => {
            message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
        }, 2000);
    }

    render() {
        const { isModalVisible } = this.props
        const { isAddGroup, data } = this.state
        return (
            <Modal
                title="添加服务器"
                visible={isModalVisible}
                footer={null}
                onCancel={this.props.onModalCancel}
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
                                    if (value === '' || value === undefined || isIP(value)) {
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
                                    if (value === '' || value === undefined || /^\d+$/.test(value)) {
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
                                message: '此账号用于系统操作服务器, 请保证此账号具有root权限',
                            }
                        ]}
                        hasFeedback
                    >
                        <Input placeholder="此账号用于系统操作服务器, 请保证此账号具有root权限" />
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
                                    if (value === '' || value === undefined || getFieldValue('password') === value) {
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
                        label="指定分组"
                        hasFeedback
                    >
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
                        >
                            {data}
                        </Select>
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
                            {data}
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

                <Modal
                    title="添加服务器分组"
                    visible={isAddGroup}
                    footer={null}
                    onCancel={() => { this.setState({isAddGroup: false}) }}
                >
                    <Form
                        name="server-group-add"
                        onFinish={this.onGroupModalOk}
                        labelCol={{ span: 6 }}
                        wrapperCol={{ span: 18 }}
                    >
                        <Form.Item
                            name="groupName"
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
                        <Form.Item
                            name="groupDes"
                            label="分组描述"
                        >
                            <Input placeholder="请输入分组描述" />
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
                                    onClick={() => {this.setState({isAddGroup: false})}}
                                >
                                    取消
                                </Button>
                            </div>
                        </Form.Item>
                    </Form>
                </Modal>

            </Modal>
        )
    }
}