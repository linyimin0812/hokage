import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select, message } from 'antd'
import { ServerAction } from '../../../axios/action/server/server-action'
import { Option } from '../../../axios/action/server/server-type'
import { formItemLayout } from '../../common/table-layout'

type SearchProps = {
    onFinish: (value: any) => void,
    clear: () => void
}

type SearchState = {
    serverOptions: Option[]
}

export default class Search extends React.Component<SearchProps, SearchState> {

    constructor(props: SearchProps) {
        super(props)
        this.state = {
            serverOptions: []
        }
    }

    componentDidMount() {
        ServerAction.listServerLabelOptions().then(options => {
            this.setState({serverOptions: options})
        }).catch((err) => {
            message.error(err)
        })
    }

    render() {
        const { serverOptions } = this.state;
        return (
            <div style={{ backgroundColor: '#FFFFFF' }}>
                <Divider orientation="left">管理员信息查询</Divider>
                <Form
                    name="operator-search"
                    onFinish={this.props.onFinish}
                >
                    <Row gutter={24}>
                        <Col span={6} key="id">
                            <Form.Item
                                name="id"
                                label="id"
                                colon
                                {...formItemLayout}
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={6} key="name">
                            <Form.Item
                                name="name"
                                label="管理员姓名"
                                colon
                                {...formItemLayout}
                            >
                                <Input placeholder="请输入" />
                            </Form.Item>
                        </Col>
                        <Col span={6} key="serverTag">
                            <Form.Item
                                name="serverTag"
                                label="服务器标签"
                                colon
                                {...formItemLayout}
                            >
                                <Select>
                                    {
                                        serverOptions.map(option => {
                                            return <Select.Option value={option.value}>{option.label}</Select.Option>
                                        })
                                    }
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