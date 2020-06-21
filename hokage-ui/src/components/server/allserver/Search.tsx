import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select } from 'antd'

type SearchPropTypes = {
    onFinish: (value: any) => void,
    clear: () => void
}

export default class Search extends React.Component<SearchPropTypes> {
    render() {
        return (
            <div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
                <Divider orientation="left">服务器信息查询</Divider>
                <Form
                    name="operator-search"
                    onFinish={this.props.onFinish}
                    style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                >
                    <Row gutter={24}>
                        <Col span={5} key="hostname">
                            <Form.Item
                                name="hoatname"
                                label="主机名"
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={5} key="admin">
                            <Form.Item
                                name="admin"
                                label="管理员"
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={5} key="serverTag">
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
                        <Col span={4} key="status">
                            <Form.Item
                                name="status"
                                label="状态"
                            >
                                <Select defaultValue="-1">
                                    <Select.Option value="-1">请选择</Select.Option>
                                    <Select.Option value="online">在线</Select.Option>
                                    <Select.Option value="offline">下线</Select.Option>
                                    <Select.Option value="exception">异常</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>
                        <Col span={5} key="submit">
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