import React from 'react'
import { Form, Select } from 'antd'
import { FormInstance } from 'antd/lib/form'
import { observer } from 'mobx-react'
import { UserVO } from '../../../axios/action/user/user-type'
import store from '../ordinary/store'

export interface SelectSupervisorProps {
  form: FormInstance,
}
@observer
export class SelectSupervisor extends React.Component<SelectSupervisorProps> {

  render() {
    const { form } = this.props
    return (
      <Form name="supervisor-select" form={form}>
        <Form.Item name="supervisorId" label={'管理员'} initialValue={[]} rules={[{ required: true, }]}>
          <Select style={{ width: '100%' }} placeholder={"请选择管理员"}>
            {store.supervisorList.map((userVO: UserVO, index: number) => {
              return <Select.Option key={index} value={userVO.id}>{`${userVO.username}(${userVO.email})`}</Select.Option>
            })}
          </Select>
        </Form.Item>
      </Form>
    )
  }
}
