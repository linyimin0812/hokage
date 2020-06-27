import React from 'react'
import { Card, Table } from 'antd'

const data: any[] = [
    {
        interface: "lo",
        IPAddress: "127.0.0.1",
        hwType: "ether",
        macAddress: "15:db:45:eb:4d:6a"
    },
    {
        interface: "enp4s0",
        IPAddress: "192.168.110.185",
        hwType: "ether",
        macAddress: "15:db:45:eb:4d:6a"
    },
    {
        interface: "docker0",
        IPAddress: "172.17.0.1",
        hwType: "ether",
        macAddress: "15:db:45:eb:4d:6a"
    },
]

export default class ARPCacheTable extends React.Component<any, any>{

    render() {
        return (
            <Card title="ARP缓存表">
                <Table dataSource={data} pagination={false} scroll={{y: "350px"}}>
                    <Table.Column title="地址" dataIndex="IPAddress" />
                    <Table.Column title="硬件类型" dataIndex="hwType" />
                    <Table.Column title="Mac地址" dataIndex="macAddress" />
                    <Table.Column title="接口名称" dataIndex="interface" />
                </Table>
            </Card>

        );
    }

}