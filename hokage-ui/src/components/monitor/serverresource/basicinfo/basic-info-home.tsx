import React from 'react'
import { Col, Divider, Row } from 'antd';
import AccountInfo from './account-info'
import BasicInfo from './basic-info'
import LoginAccountInfo from './login-account-info';

export default class BasicInfoHome extends React.Component<any, any>{
    render() {
        return (
            <div>
                <Row gutter={12}>
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

                <Row gutter={12}>
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