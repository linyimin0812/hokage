import React from 'react'
import { Divider, Form, Row, Col, Input, Button, Select } from 'antd'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import { useServerOptions } from '../../../hook'

type SearchPropTypes = {
  onFinish: (value: ServerSearchForm) => void,
}

export const AllServerSearch = (props: SearchPropTypes) => {

  const [form] = Form.useForm()
  const [serverGroupOptions] = useServerOptions()

  const onFinish = (value: ServerSearchForm) => { props.onFinish(value) }
  const onReset = () => { form.resetFields() }

  return (
    <div style={{ backgroundColor: '#FFFFFF'}}>
      <Divider orientation="left">服务器信息查询</Divider>
      <Form
        name="operator-search"
        onFinish={onFinish}
        style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
        form={form}
      >
        <Row gutter={24} style={{ width: '100%' }}>
          <Col span={6} key="hostname">
            <Form.Item name="hostname" label="主机名">
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="domain">
            <Form.Item name="domain" label="域名">
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>

          <Col span={6} key="ipAddress">
            <Form.Item name="ip" label="ip地址">
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>

          <Col span={6} key="supervisor">
            <Form.Item name="supervisor" label="管理员">
              <Input placeholder="请输入" />
            </Form.Item>
          </Col>
          <Col span={6} key="serverGroup">
            <Form.Item name="serverGroup" label="服务器分组">
              <Select mode={'multiple'}>
                {serverGroupOptions.map(option => {
                  return <Select.Option value={option.value}>{option.label}</Select.Option>
                })}
              </Select>
            </Form.Item>
          </Col>
          <Col span={4} key="submit">
            <Button type="primary" htmlType="submit">Search</Button>
            <Button style={{ margin: '0 8px' }} htmlType="button" onClick={onReset}>Clear</Button>
          </Col>
        </Row>
      </Form>
    </div>
  )
}
