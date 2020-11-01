import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select } from 'antd'

type SearchPropTypes = {
    onFinish: (value: any) => void,
    clear: () => void
}

export default class Search extends React.Component<SearchPropTypes> {
    render() {
        return (
            <div style={{ backgroundColor: '#FFFFFF' }}>
                <Divider orientation="left">管理员信息查询</Divider>
                <Form
                    name="operator-search"
                    onFinish={this.props.onFinish}
                    style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                >
                    <Row gutter={24}>
                        <Col span={6} key="id">
                            <Form.Item
                                name="id"
                                label="id"
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={6} key="name">
                            <Form.Item
                                name="name"
                                label="姓名"
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={6} key="serverTag">
                            <Form.Item
                                name="serverTag"
                                label="标签"
                            >
                                <Select defaultValue="-1">
                                    <Select.Option value="-1">请选择</Select.Option>
                                    <Select.Option value="ordinaryServer">普通服务器</Select.Option>
                                    <Select.Option value="gpuServer">GPU服务器</Select.Option>
                                    <Select.Option value="intranetServer">内网服务器</Select.Option>
                                    <Select.Option value="publicNetworkServer">外网服务器</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>
                        <Col span={6} key="submit">
                            <Button type="primary" htmlType="submit">
                                Search
                            </Button>
                            <Button
                                style={{
                                    margin: '0 8px',
                                }}
                                onClick={() => {
                                    this.props.clear();
                                }}
                            >
                                Clear
                            </Button>
                        </Col>
                    </Row>
                </Form>
            </div>
        )
    }
}