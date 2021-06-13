import React from 'react'
import { FileProperty } from '../../axios/action/file-management/file-management-type'
import FileTable from './table'
import { Button, Form, Input, message, Modal } from 'antd';
import store from './store'
import { observer } from 'mobx-react'
import path from 'path'
import { DeleteFilled } from '@ant-design/icons'

export interface ActionProps {
  visible: boolean,
  left?: string,
  top?: string,
  record?: FileProperty,
  fileTable?: FileTable
}

type ActionState = {
  modalVisible: boolean
}

@observer
export default class MenuContext extends React.Component<ActionProps, ActionState> {

  state = {
    modalVisible: false
  }

  onOpenClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('打开失败')
    }
    store.actionProps.visible = false
    fileTable!.onDoubleClick(record!)
  }

  onDownloadClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('下载失败')
    }
    store.actionProps.visible = false
    fileTable!.downloadFile(record!)
  }

  onDeleteClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('删除失败')
    }
    store.actionProps.visible = false
    const type = record!.type === 'file' ? '文件' : '文件夹'
    Modal.confirm({
      onOk: () => { fileTable!.removeFile(record!) },
      okText: 'confirm',
      cancelText: 'cancel',
      content: `确定删除${type}: ${path.resolve(record?.curDir!, record?.name!)}`,
      title: <span><DeleteFilled translate style={{color: 'red'}} /></span>
    })
  }

  onRenameClick = () => {
    store.actionProps.visible = false
    this.setState({modalVisible: true})
  }

  renameFileFinish = (value: { newName: string }) => {
    const { record, fileTable } = this.props
    const dst = path.resolve(record?.curDir!, value.newName)
    fileTable!.moveFile(record!, dst)
    this.setState({modalVisible: false})
  }


  specifyMenuOptionClassName = () => {
    const { record } = this.props
    const menuOption = 'contextMenu--option'
    const menuOptionDisabled = 'contextMenu--option contextMenu--option__disabled'
    if (['.', '..'].includes(record!.name)) {
      return menuOptionDisabled
    }
    return menuOption
  }

  render() {
    const { visible, left, top, record } = this.props
    const { modalVisible } = this.state
    if (!visible) {
      return (
        <Modal title="添加服务器分组" visible={modalVisible} footer={null} closable={false}>
          <Form name="server-group-add" onFinish={this.renameFileFinish} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
            <Form.Item name="name" label="原名称">
              <Input placeholder="请输入分组名称" defaultValue={record?.name} disabled />
            </Form.Item>
            <Form.Item name="newName" label="新名称" required>
              <Input placeholder="请新名称" defaultValue={record?.name} />
            </Form.Item>
            <Form.Item wrapperCol={{ span: 24 }}>
              <div style={{textAlign: 'center'}}>
                <Button type="primary" htmlType="submit">确认</Button>
                <Button style={{ margin: '0 8px', }} onClick={() => {this.setState({modalVisible: false})}}>取消</Button>
              </div>
            </Form.Item>
          </Form>
        </Modal>
      )
    }

    return (
      <div className={'contextMenu'} style={{ left: left, top: top }}>
        <div className={'contextMenu--option'} onClick={this.onOpenClick}>打开</div>
        <div className={this.specifyMenuOptionClassName()} onClick={this.onDownloadClick}>下载</div>
        <div className={'contextMenu--separator'} />
        <div className={this.specifyMenuOptionClassName()} onClick={this.onRenameClick}>重命名</div>
        <div className={'contextMenu-separator'} />
        <div className={'contextMenu--option contextMenu--option__disabled'}>分享</div>
        <div className={'contextMenu--separator'} />
        <div className={'contextMenu--option contextMenu--option__disabled'}>权限</div>
        <div className={'contextMenu--option contextMenu--option__disabled'}>压缩</div>
        <div className={'contextMenu--separator'} />
        <div className={this.specifyMenuOptionClassName()} onClick={this.onDeleteClick}>删除</div>
      </div>
    )
  }
}
