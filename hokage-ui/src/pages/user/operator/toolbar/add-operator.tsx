import React from 'react';
import { Modal, Form, Select, Button } from 'antd';
import { Option } from '../../../../axios/action/server/server-type'
import { observer } from 'mobx-react'
import store from '../store'

type AddOperatorPropTypes = {
  onModalOk: (value: number[]) => void,
  onModalCancel: () => void,
}

export default observer((props: AddOperatorPropTypes) => {

  const [form] = Form.useForm()

  const onModalOk = (idList: number[]) => {
    props.onModalOk(idList)
    form.resetFields()
    store.fetchRecords()
  }

  const reset = () => {
    props.onModalCancel()
    form.resetFields()
  }

  return (
    <Modal title="批量添加管理员" visible={store.isModalVisible} footer={null} onCancel={props.onModalCancel}>
      <Form name="operator-add" onFinish={onModalOk} form={form}>
        <Form.Item label={'管理员'} name="userIds" initialValue={[]} rules={[{required: true}]}>
          <Select mode="multiple" style={{ width: '100%' }} placeholder={"请选择(支持多选)"}>
            {store.userOptions.map((option: Option, index) => {
              return <Select.Option key={index} value={option.value}>{option.label}</Select.Option>
            })}
          </Select>
        </Form.Item>
        <Form.Item style={{textAlign: 'center'}}>
          <Button type="primary" htmlType="submit">添加</Button>
          <Button style={{ margin: '0 8px' }} onClick={reset}>取消</Button>
        </Form.Item>
      </Form>
    </Modal>
  )
})

