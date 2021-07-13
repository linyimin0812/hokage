import React from 'react'
import { Form, Select } from 'antd'
import { ServerVO } from '../../../axios/action/server/server-type'
import { FormInstance } from 'antd/lib/form'
import { observer } from 'mobx-react';
import store from '../store';

export interface SelectServerProps {
  form: FormInstance,
}
@observer
export class SelectServer extends React.Component<SelectServerProps> {

  render() {
    const { form } = this.props
    return (
      <Form name="server-select" form={form}>
        <Form.Item name="serverIds" label={'服务器'} initialValue={[]} rules={[{ required: true, }]}>
          <Select mode="multiple" style={{ width: '100%' }} placeholder={"请选择(支持多选)"}>
            {store.serverList.map((serverVO: ServerVO, index) => {
              return <Select.Option key={index} value={serverVO.id}>{`${serverVO.account}@${serverVO.ip}`}</Select.Option>
            })}
          </Select>
        </Form.Item>
      </Form>
    )
  }
}
