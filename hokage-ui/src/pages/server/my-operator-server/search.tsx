import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select } from 'antd'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import { useServerOptions } from '../../../hook'

type SearchPropTypes = {
  onFinish: (value: any) => void
}

export const MyOperatorServerSearch = (props: SearchPropTypes) => {

  const [form] = Form.useForm()

  const [serverOptions] = useServerOptions()
  const onFinish = (value: ServerSearchForm) => { props.onFinish(value) }
  const onReset = () => { form.resetFields() }

  return (
    <div style={{ backgroundColor: '#FFFFFF' }}>
      <Divider orientation="left">服务器信息查询</Divider>
      <Form
        name="operator-search"
        onFinish={onFinish}
        style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
        form={form}
      >
        <Row gutter={24} style={{ width: '100%' }}>
          <Col span={6} key="hostname">
            <Form.Item
              name="hostname"
              label="主机名"
            >
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="domain">
            <Form.Item
              name="domain"
              label="域名"
            >
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="ipAddress">
            <Form.Item
              name="ip"
              label="ip地址"
            >
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="serverGroup">
            <Form.Item
              name="serverGroup"
              label="服务器分组"
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
          <Col span={6} key="username">
            <Form.Item
              name="username"
              label="用户名"
            >
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="account">
            <Form.Item
              name="account"
              label="登录账号"
            >
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={4} key="submit">
            <Button type="primary" htmlType="submit">
              Search
            </Button>
            <Button
              style={{
                margin: '0 8px',
              }}
              onClick={onReset}
              htmlType="button"
            >
              Clear
            </Button>
          </Col>
        </Row>
      </Form>
    </div>
  )
}
