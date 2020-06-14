import React from 'react'
import { Table, Tag } from 'antd';
import EditBatCommand from './EditBatCommand';
import ExecedBatCommandInfo from './ExecedBatCommandInfo';

interface ExecutedBatCommandStateType {
    isModalVisible: boolean, // 弹窗显示任务信息
    expandedRowKeys: string[]
    isDrawerVisible: boolean, // 单机任务详细信息
}

const status2color = {
    success: 'green',
    fail: 'red',
    processing: 'blue'
}

export default class ExecutedBatCommand extends React.Component<any, ExecutedBatCommandStateType> {

    state = {
        isModalVisible: false,
        expandedRowKeys: [],
        isDrawerVisible: false
    }

    statusRender = (status: string) => {
        if (Object.keys(status2color).includes(status)) {
            //@ts-ignore
            return <Tag color={status2color[status]}>{status}</Tag>
        }
        return <Tag color={status2color.processing}>unknown</Tag>
    }

    renderAction = (id: string | number) => {
        return <span onClick={() => { this.viewTask(id) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
    }

    renderNestedTableAction = (id: string | number) => {
        return <span onClick={() => { this.viewDetailTask(id) }} style={{color:"#5072D1", cursor: "pointer"}}>查看任务详情</span>
    }

    viewTask = (id: string | number) => {
        // TODO: 获取具体的任务详情
        this.setState({ isModalVisible: true })
    }

    viewDetailTask = (id: string | number) => {
        // TODO: 获取每个机器上的具体的任务详情
        this.setState({ isDrawerVisible: true })
    }

    onExpand = (expanded: boolean, record: any) => {
        console.log(record)
        if (expanded) {
            this.setState({expandedRowKeys: [record.id]})
        } else {
            this.setState({ expandedRowKeys: [] })
        }
    }

    expandedRowRender = (record: any) => {
        const id: string | number = record.id
        console.log(id)
        // TODO:根据id获取所有机器的任务执行信息

        const data = [1,2,3,4,5,6].map(value => {
            return { server: `10.108.210.19${value}`, startTime: new Date().toISOString(), execedTime: '少于1秒', status: 'processing', id: value }
        })
        return (
            <Table pagination={false} dataSource={data}>
                <Table.Column title="执行服务器" dataIndex="server" key="server" />
                <Table.Column title="开始时间" dataIndex="startTime" key="startTime" />
                <Table.Column title="执行时长" dataIndex="execedTime" key="execedTime" />
                <Table.Column title="执行状态" dataIndex="status" render={this.statusRender} key="status" />
                <Table.Column title="操作" dataIndex="id" render={this.renderNestedTableAction} key="id" />
            </Table>
        )
    }

    onCloseDrawer = () => {
        this.setState({ isDrawerVisible: false })
    }

    // TODO: 根据任务id获取任务的执行信息

    render() {

        const data = [1,2,3,4,5].map(value => {
            return {exitCode: 0, key: value, id: value, name: `task_${value}`, status: `success`, startTime: new Date().toISOString(), execedTime: "少于1秒", action: '查看任务'}
        })

        const { isModalVisible, isDrawerVisible, expandedRowKeys } = this.state

        return (

            <div>
                {/*可扩展表,子表显示机器, 可参考阿里云远程命令的格式*/}
                <Table dataSource={data} onExpand={this.onExpand} expandedRowRender={this.expandedRowRender} expandedRowKeys={expandedRowKeys} >
                    <Table.Column title="任务ID" dataIndex="id" />
                    <Table.Column title="任务名称" dataIndex="name" />
                    <Table.Column title="执行状态" dataIndex="status" render={this.statusRender} />
                    <Table.Column title="开始时间" dataIndex="startTime" />
                    <Table.Column title="执行时长" dataIndex="execedTime" />
                    <Table.Column title="ExitCode" dataIndex="exitCode" />
                    <Table.Column title="操作" dataIndex="id" render={this.renderAction} />
                </Table>
                <EditBatCommand isEdit={false} isVisible={isModalVisible} onChange={(value: boolean) => {this.setState({isModalVisible: value})}} />
                <ExecedBatCommandInfo isVisible={isDrawerVisible} onCloseDrawer={(value: boolean) => {this.setState({isDrawerVisible: value})}} />
            </div>
        );
    }

}