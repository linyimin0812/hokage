import React from 'react'
import { Table } from 'antd';

export default class ExecutedBatCommand extends React.Component {


    render() {

        return (
            <div>
                {/*可扩展表,子表显示机器, 可参考阿里云远程命令的格式*/}
                <Table dataSource={[]}>
                    <Table.Column title="任务名称" dataIndex="name"/>
                    <Table.Column title="执行状态" dataIndex="status" />
                    <Table.Column title="开始时间" dataIndex="startTime" />
                    <Table.Column title="执行时长" dataIndex="execedTime" />
                    <Table.Column title="操作" dataIndex="action" />
                </Table>
            </div>
        );
    }

}