import React from 'react'
import { Table } from 'antd';

const data: any[] = [
    {
        pid: 123,
        user: "banzhe",
        memory: "12.21%",
        rss: "12.20MB",
        VSZ: "923KB",
        cmd: "nginx"
    },
    {
        pid: 123,
        user: "banzhe",
        memory: "12.21%",
        rss: "12.20MB",
        VSZ: "923KB",
        cmd: "nginx"
    },
]

export default class RamProcess extends React.Component<any, any>{

    render() {
        return (
            <Table dataSource={data} pagination={false}>
                <Table.Column title="PID" dataIndex="pid" />
                <Table.Column title="USER" dataIndex="user" />
                <Table.Column title="MEM%" dataIndex="memory" />
                <Table.Column title="RSS" dataIndex="rss" />
                <Table.Column title="VSZ" dataIndex="vsz" />
                <Table.Column title="CMD" dataIndex="cmd" />
            </Table>
        );
    }
}