import React from 'react'
import { Col, Divider, Row } from 'antd';
import AverageLoad from './AverageLoad'
import CpuUtilization from './CpuUtilization'
import RamUsage from './RamUsage'
import CpuProcess from './CpuProcess';
import RamProcess from './RamProcess';
import DiskPartition from './DiskPartition';

export default class SystemStatus extends React.Component<any, any>{
    state = {

    }
    render() {
        return (
            <div>
                <Row gutter={12} align="middle" justify={"center"} >
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
                <Divider />
                <Row gutter={12} >
                    <Col span={8}>
                        <CpuProcess />
                    </Col>
                    <Col span={8}>
                        <RamProcess />
                    </Col>
                    <Col span={8}>
                        <DiskPartition />
                    </Col>
                </Row>
            </div>
        )
    }
}