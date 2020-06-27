import React from 'react'
import { Card, Table } from 'antd'

const data: any[] = [
    {
        pid: 123,
        user: "banzhe",
        cpu: "12.21%",
        rss: "12.20MB",
        VSZ: "923KB",
        cmd: "nginx"
    },
    {
        pid: 123,
        user: "banzhe",
        cpu: "12.21%",
        rss: "12.20MB",
        VSZ: "923KB",
        cmd: "nginx"
    },
]

export default class CpuProcess extends React.Component<any, any>{

    render() {
        return (
            <Card title="CPU使用率">
                <Table dataSource={data} pagination={false} scroll={{y: 350}} >
                    <Table.Column title="PID" dataIndex="pid" />
                    <Table.Column title="USER" dataIndex="user" />
                    <Table.Column title="CPU%" dataIndex="cpu" />
                    <Table.Column title="RSS" dataIndex="rss" />
                    <Table.Column title="VSZ" dataIndex="vsz" />
                    <Table.Column title="CMD" dataIndex="cmd" />
                </Table>
            </Card>
        );
    }
}