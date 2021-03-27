import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select } from 'antd'
import { formItemLayout } from '../../common/table-layout'
import { useServerOptions } from '../hook'

interface SearchFormType {
    id: string,
    username: string,
    serverGroup: string[]
}

type SearchProps = {
    onFinish: (value: Partial<SearchFormType>) => void
}

export type OperatorSearchFormType = Partial<SearchFormType>

export const OperatorSearch = (props: SearchProps) => {

    const [serverOptions] = useServerOptions()
    const [form] = Form.useForm()

    const onFinish = (formValue: Partial<SearchFormType>) => {
        props.onFinish(formValue)
    }

    const onReset = () => { form.resetFields() }

    return (
        <div style={{ backgroundColor: '#FFFFFF' }}>
            <Divider orientation="left">管理员信息查询</Divider>
            <Form
                name="operator-search"
                onFinish={onFinish}
                form={form}
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
                    <Col span={6} key="username">
                        <Form.Item
                            name="username"
                            label="管理员姓名"
                            colon
                            {...formItemLayout}
                        >
                            <Input placeholder="请输入" />
                        </Form.Item>
                    </Col>
                    <Col span={6} key="serverGroup">
                        <Form.Item
                            name="serverGroup"
                            label="服务器分组"
                            colon
                            {...formItemLayout}
                        >
                            <Select mode={'multiple'}>
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
                            htmlType="button"
                            onClick={onReset}
                        >
                            Clear
                        </Button>
                    </Col>
                </Row>
            </Form>
        </div>
    )

}