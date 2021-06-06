import React from 'react'
import { Button, Modal } from 'antd'
import './index.less'

export interface ActionModalProps {
  title: string | JSX.Element,
  action: () => void,
  content: string
}

export const ConfirmModal = (props: ActionModalProps) => {

  const onClick = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.stopPropagation()
    Modal.confirm({
      onOk: () => { props.action() },
      okText: 'confirm',
      cancelText: 'cancel',
      content: props.content,
      title: props.title
    })
  }
  return (
    <Button type="link" onClick={onClick}>{props.title}</Button>
  )
}
