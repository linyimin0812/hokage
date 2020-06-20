import React from 'react'
import { Card, Table } from 'antd';

const data: {[key: string]: any}[] = [
    {
        name: "Hostname",
        value: "webserver-prod-983742"
    },
    {
        name: "OS",
        value: "Ubuntu 16.04.2 LTS 4.4.0-77-generic",
    },
    {
        name: "Server Time",
        value: "Sat May 13 00:11:39 EST 2017",
    },
    {
        name: "Uptime",
        value: "5 hours and 32 minutes and 27 seconds"
    }
]

interface BasicInfoPropsType {
    title: string
}

export default class BasicInfo extends React.Component<BasicInfoPropsType, any> {
    render() {
        return (
            <Card title={this.props.title}>
                <Table dataSource={data} pagination={false} showHeader={false}>
                    <Table.Column dataIndex="name" />
                    <Table.Column dataIndex="value" />
                </Table>
            </Card>
        );
    }
}