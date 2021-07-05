import React, { useState } from 'react'
import { Button, Form, Modal } from 'antd'
import { FormInstance } from 'antd/lib/form'
import { UserServerOperateForm } from '../axios/action/user/user-type'

export interface FormModalProps {
  title: string,
  renderForm: (form: FormInstance) => React.ReactNode,
  confirmAction: (formValue: UserServerOperateForm) => void,
  cancelAction?: () => void,
  onClickAction?: () => void
}

export const FormModal = (props: FormModalProps) => {
  const [form] = Form.useForm()
  const [visible, setVisible] = useState(false)
  const [confirmLoading, setConfirmLoading] = useState(false)

  const onClick = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.stopPropagation()
    setVisible(true)
    if (props.onClickAction) {
      props.onClickAction()
    }
  }

  const onCancel = () => {
    const { cancelAction } = props
    form.resetFields()
    setVisible(false)
    if (cancelAction) {
      cancelAction()
    }
  }
  const onOk = (_: React.MouseEvent<HTMLElement>) => {
    setConfirmLoading(true)
    const confirmAction = async () => {
      let formValue: UserServerOperateForm = {}
      try {
        formValue = await form.validateFields()
        await props.confirmAction(formValue)
        form.resetFields()
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
        visible={visible} title={props.title} okText={'confirm'} cancelText={'cancel'}
        onCancel={onCancel} confirmLoading={confirmLoading}
        onOk={onOk} closable={false} maskClosable={false}
      >
        {props.renderForm(form)}
      </Modal>
    </>
  )
}
