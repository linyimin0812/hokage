import React from 'react'
import { Form, Select } from 'antd'
import { ServerVO } from '../../axios/action/server/server-type'
import { useServerList } from '../../hook'
import { FormInstance } from 'antd/lib/form'

export interface SelectServerProps {
  form: FormInstance
}

export const SelectServer = (props: SelectServerProps) => {
  const [serverList] = useServerList()

  return (
    <Form name="server-select" form={props.form}>
      <Form.Item name="serverIds" label={'服务器'} initialValue={[]} rules={[{ required: true, }]}>
        <Select mode="multiple" style={{ width: '100%' }} placeholder={"请选择(支持多选)"}>
          {serverList.map((serverVO: ServerVO, index) => {
            return <Select.Option key={index} value={serverVO.id}>{serverVO.ip}</Select.Option>
          })}
        </Select>
      </Form.Item>
    </Form>
  )
}
