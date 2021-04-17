import React from 'react'
import { Link as ALink } from 'react-router-dom'
import { Button as AButton, Divider } from 'antd'
import { hasPermissions } from '../libs'
import { ActionModalProps, ConfirmModal } from './confirm-modal'
import { FormModal, FormModalProps } from './form-modal'

export class Action extends React.Component {
  static Link(props: any) {
    return <ALink {...props} />
  }

  static Button(props: any) {
    return <AButton type={'link'} {...props} style={{padding: 0}} />
  }

  static Confirm(props: ActionModalProps) {
    return <ConfirmModal {...props} />
  }

  static Form(props: FormModalProps) {
    return <FormModal {...props} />
  }

  _canVisible = (path: string): boolean => {
    return !path || hasPermissions(path)
  }

  _handle = (children: React.ReactElement[], el: React.ReactElement) => {
    const length = children.length
    if (this._canVisible(el.props.auth)) {
      if (length !== 0) {
        children.push(<Divider key={length} type={'vertical'} />)
      }

      children.push(el)
    }
  }

  render() {
    const children: React.ReactElement[] = []
    const propsChildren: React.ReactElement[] = Array.isArray(this.props.children) ? this.props.children as React.ReactElement[] : [this.props.children as React.ReactElement]
    propsChildren.forEach(el => this._handle(children, el))
    return (children)
  }
}
