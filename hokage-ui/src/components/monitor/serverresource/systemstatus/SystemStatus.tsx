import React from 'react'
import { Col, Row } from 'antd'
import AverageLoad from './AverageLoad'
import CpuUtilization from './CpuUtilization'
import RamUsage from './RamUsage'

export default class SystemStatus extends React.Component<any, any>{
    state = {

    }
    render() {
        return (
            <div>
                <Row gutter={24} align="middle" justify={"center"} >
                    <Col span={8}>
                        <AverageLoad />
                    </Col>
                    <Col span={8}>
                        <CpuUtilization />
                    </Col>
                    <Col span={8}>
                        <RamUsage />
                    </Col>
                </Row>
            </div>
        )
    }
}