import React from 'react'
import { Card, Table } from 'antd';

const data: any[] = [
    {
        name: "udev",
        status: "224.5G / 449G",
        used: "12.21%",
        mount: "/dev",
    },
    {
        name: "udev",
        status: "224.5G / 449G",
        used: "12.21%",
        mount: "/dev",
    },
]

export default class DiskPartition extends React.Component<any, any>{

    render() {
        return (
            <Card title="磁盘使用率">
                <Table dataSource={data} pagination={false} scroll={{y: 350}} >
                    <Table.Column title="NAME" dataIndex="name" />
                    {/*渲染进度条*/}
                    <Table.Column title="STATUS" dataIndex="status" />
                    <Table.Column title="USED%" dataIndex="used" />
                    <Table.Column title="MOUNT" dataIndex="mount" />
                </Table>
            </Card>
        );
    }
}