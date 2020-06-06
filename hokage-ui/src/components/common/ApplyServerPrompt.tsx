import React from 'react';
import { Button, Divider, message, Result } from 'antd';
import { PlusOutlined } from '@ant-design/icons/lib';
import AddServer from '../server/AddServer';

interface ApplyServerPromptStateType {
    isModalVisible: boolean
}

export default class ApplyServerPrompt extends React.Component<any, ApplyServerPromptStateType> {

    state = {
        isModalVisible: false
    }

    applyServer = () => {
        window.location.href = "/#/app/server/all"
    }

    add = () => {
        this.setState({isModalVisible: true })
        console.log('hahahahaha')
    }

    onModalOk = (value: any) => {
        console.log(value)
        this.setState({ isModalVisible: false })
        message.loading({ content: 'Loading...', key: 'addServer' });
        // TODO 保存服务器信息
        setTimeout(() => {
            message.success({ content: 'Loaded!', key: 'addServer', duration: 2 });
        }, 2000);
    }

    onModalCancel = () => {
        this.setState({ isModalVisible: false })
        message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
    }

    render() {
        const { isModalVisible } = this.state
        return (
            <>
                <Result
                    title="你还没有可用服务器哦,请点击申请按钮进行申请,或者点击添加按钮进行添加"
                    extra={[
                        <Button
                            key="1"
                            icon={<PlusOutlined translate="true" />}
                            onClick={this.applyServer}
                        >
                            申请
                        </Button>,
                        <Divider type="vertical" />,
                        <Button
                            key="2"
                            icon={<PlusOutlined translate="true" />}
                            onClick={this.add}
                        >
                            添加
                        </Button>
                    ]}
                />
                <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
            </>
        )
    }
}