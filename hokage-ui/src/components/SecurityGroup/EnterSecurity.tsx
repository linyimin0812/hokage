import React from 'react'
import { Table } from 'antd';

export default class EnterSecurity extends React.Component {
    render() {
        // 一个可编辑的表格
        return (
            <div>
                <Table >
                    <Table.Column title="服务器(组)" dataIndex="servers" />
                    <Table.Column title="授权策略" dataIndex="authStrategy" />
                    <Table.Column title="协议类型" dataIndex="protocolType" />
                    <Table.Column title="端口范围" dataIndex="portRange" />
                    <Table.Column title="授权类型" dataIndex="authType" />
                    <Table.Column title="授权对象" dataIndex="authObject" />
                    <Table.Column title="描述" dataIndex="description" />
                    <Table.Column title="创建时间" dataIndex="createTime" />
                    <Table.Column title="操作" dataIndex="action" />
                </Table>
            </div>
        );
    }
}