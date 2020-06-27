import React from 'react'
import { Card, Table } from 'antd'

const data: any[] = [
    {
        localAddress: "127.0.0.1:45242",
        foreignAddress: "222.186.174.149:17326",
        state: "ESTABLISHED",
        pid: "5089/chrome"
    },
    {
        localAddress: "192.168.110.185:38990",
        foreignAddress: "127.0.0.1:1080",
        state: "TIME_WAIT",
        pid: "17723/java"
    },
    {
        localAddress: "127.0.0.1:48342",
        foreignAddress: "108.177.125.188:443",
        state: "ESTABLISHED",
        pid: "3337/python"
    },
]

export default class ConnectionInfo extends React.Component<any, any>{

    render() {
        return (
            <Card title="网络连接信息表">
                <Table dataSource={data} pagination={false} scroll={{y: "350px"}}>
                    <Table.Column title="本地地址" dataIndex="localAddress" />
                    <Table.Column title="连接地址" dataIndex="foreignAddress" />
                    <Table.Column title="连接状态" dataIndex="state" />
                    <Table.Column title="进程" dataIndex="pid" />
                </Table>
            </Card>

        );
    }

}