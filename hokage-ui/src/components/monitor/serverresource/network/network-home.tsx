import React from 'react'
import { Col, Divider, Row } from 'antd'
import InterfaceInfo from './interface-info'
import DownloadSpeed from './download-speed'
import UploadSpeed from './upload-speed'
import ARPCacheTable from './arpc-cache-table'
import ConnectionInfo from './connection-info'

export default class NetworkHome extends React.Component<any, any>{
    render() {
        return (
            <div>
                <Row gutter={12}>
                    <Col span={8}>
                        <InterfaceInfo />
                    </Col>
                    <Col span={8}>
                        <ARPCacheTable />
                    </Col>
                    <Col span={8}>
                        <ConnectionInfo />
                    </Col>

                </Row>
                <Divider />
                <Row>
                    <Col span={10}>
                        <DownloadSpeed />
                    </Col>
                    <Col span={2} />
                    <Col span={10}>
                        <UploadSpeed />
                    </Col>
                    <Col span={2} />
                </Row>
            </div>
        )
    }
}