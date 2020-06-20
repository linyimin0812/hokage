import React from 'react'
import { Card, Table } from 'antd';

const data: any[] = [
    {
        type: "system",
        username: "banzhe",
        homeDir: "/home/banzhe"
    },
    {
        type: "system",
        username: "root",
        homeDir: "/home/root"
    },
    {
        type: "user",
        username: "linyimin",
        homeDir: "/home/linyimin"
    },
]

export default class AccountInfo extends React.Component<any, any> {
    render() {
        return (
            <Card title="账户信息">
                <Table dataSource={data} pagination={false} scroll={{y: 350}} >
                    <Table.Column title="类型" dataIndex="type" />
                    <Table.Column title="用户名" dataIndex="username" />
                    <Table.Column title="工作目录" dataIndex="homeDir" />
                </Table>
            </Card>
        );
    }
}