import React from 'react'
import { Col, Divider, Row } from 'antd';
import AccountInfo from './AccountInfo'
import BasicInfo from './BasicInfo'
import LoginAccountInfo from './LoginAccountInfo';

export default class BasicInfoHome extends React.Component<any, any>{
    render() {
        return (
            <div>
                <Row>
                    <Col span={8}>
                        <BasicInfo title={"基本信息"} />
                    </Col>
                    <Col span={8}>
                        <AccountInfo />
                    </Col>
                    <Col span={8}>
                        <LoginAccountInfo />
                    </Col>
                </Row>

                <Divider />

                <Row>
                    <Col span={8}>
                        <BasicInfo title={"CPU信息"} />
                    </Col>
                    <Col span={8}>
                        <BasicInfo title={"内存信息"} />
                    </Col>
                    <Col span={8}>
                        <BasicInfo title={"磁盘信息"} />
                    </Col>
                </Row>
            </div>
        );
    }
}