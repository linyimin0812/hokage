import React, { useState } from 'react'
import { Button, Form, Modal } from 'antd'
import { FormInstance } from 'antd/lib/form'
import { UserServerOperateForm } from '../axios/action/user/user-type'

export interface FormModalProps {
  title: string,
  renderForm: (form: FormInstance) => React.ReactNode,
  action: (formValue: UserServerOperateForm) => void
}

export const FormModal = (props: FormModalProps) => {
  const [form] = Form.useForm()
  const [visible, setVisible] = useState(false)
  const [confirmLoading, setConfirmLoading] = useState(false)

  const onClick = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.stopPropagation()
    setVisible(true)
  }

  const onCancel = () => {
    form.resetFields()
    setVisible(false)
  }
  const onOk = (_: React.MouseEvent<HTMLElement>) => {
    setConfirmLoading(true)
    const confirmAction = async () => {
      let formValue: UserServerOperateForm = {}
      try {
        formValue = await form.validateFields()
        await props.action(formValue)
        setVisible(false)
      } finally {
        setConfirmLoading(false)
      }
    }
    confirmAction()
  }


  return (
    <>
      <Button type="link" onClick={onClick}>{props.title}</Button>
      <Modal
        visible={visible}
        title={props.title}
        okText={'confirm'}
        cancelText={'cancel'}
        onCancel={onCancel}
        confirmLoading={confirmLoading}
        onOk={onOk}
        closable={false}
        maskClosable={false}
      >
        {props.renderForm(form)}
      </Modal>
    </>
  )
}
