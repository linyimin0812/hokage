import React from 'react'
import { Card, Table } from 'antd';

const data: any[] = [
    {
        type: "system",
        username: "banzhe",
        loginTime: "2020.06.20 22:23:05"
    },
    {
        type: "system",
        username: "root",
        loginTime: "2020.06.20 23:20:05"
    },
    {
        type: "user",
        username: "linyimin",
        loginTime: "2020.06.18 10:23:05"
    },
]

export default class LoginAccountInfo extends React.Component<any, any> {
    render() {
        return (
            <Card title="登录账户信息">
                <Table dataSource={data} pagination={false}>
                    <Table.Column title="类型" dataIndex="type" />
                    <Table.Column title="用户名" dataIndex="username" />
                    <Table.Column title="登录时间" dataIndex="loginTime" />
                </Table>
            </Card>
        );
    }
}