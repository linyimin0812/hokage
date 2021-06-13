import React from 'react'
import { FileProperty } from '../../axios/action/file-management/file-management-type'
import FileTable from './table'
import { Button, Checkbox, Form, Input, message, Modal } from 'antd';
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
  renameModalVisible: boolean,
  permissionModalVisible: boolean
}

const permissionOptions = [{label: 'readable', value: 4}, {label: 'writable', value: 2}, {label: 'executable', value: 1}]

@observer
export default class MenuContext extends React.Component<ActionProps, ActionState> {

  state = {
    renameModalVisible: false,
    permissionModalVisible: false
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
    this.setState({renameModalVisible: true})
  }

  renameFileFinish = (value: { newName: string }) => {
    const { record, fileTable } = this.props
    const dst = path.resolve(record?.curDir!, value.newName)
    fileTable!.moveFile(record!, dst)
    this.setState({renameModalVisible: false})
  }

  onCompressClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('压缩失败')
    }
    store.actionProps.visible = false
    fileTable!.tarDirectory(record!)
  }

  onPermissionClick = () => {
    store.actionProps.visible = false
    this.setState({permissionModalVisible: true})
  }

  changePermissionFinish = (value: {owner: number[], group: number[], other: number[]}) => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('更改权限失败')
    }
    const owner: string = value.owner ? value.owner.reduce((prev, cur) => prev + cur).toString() : '0'
    const group: string = value.group ? value.group.reduce((prev, cur) => prev + cur).toString() : '0'
    const other: string = value.other ? value.other.reduce((prev, cur) => prev + cur).toString() : '0'
    const permission = owner + group + other
    fileTable!.changeFilePermission(permission, record!)
    this.setState({permissionModalVisible: false})

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
    const { renameModalVisible, permissionModalVisible } = this.state
    if (!visible) {
      return (
        <>
          <Modal title="文件重命名" visible={renameModalVisible} footer={null} closable={false}>
            <Form name="server-file-rename" onFinish={this.renameFileFinish} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
              <Form.Item name="name" label="原名称">
                <Input placeholder="请输入分组名称" defaultValue={record?.name} disabled />
              </Form.Item>
              <Form.Item name="newName" label="新名称" required>
                <Input placeholder="请新名称" defaultValue={record?.name} />
              </Form.Item>
              <Form.Item wrapperCol={{ span: 24 }}>
                <div style={{textAlign: 'center'}}>
                  <Button type="primary" htmlType="submit">确认</Button>
                  <Button style={{ margin: '0 8px', }} onClick={() => {this.setState({renameModalVisible: false})}}>取消</Button>
                </div>
              </Form.Item>
            </Form>
          </Modal>
          <Modal title="权限设置" visible={permissionModalVisible} footer={null} closable={false}>
            <Form name="server-file-rename" onFinish={this.changePermissionFinish} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
              <Form.Item name="owner" label="拥有者">
                <Checkbox.Group options={permissionOptions} />
              </Form.Item>
              <Form.Item name="group" label="组">
                <Checkbox.Group options={permissionOptions} />
              </Form.Item>
              <Form.Item name="other" label="其他">
                <Checkbox.Group options={permissionOptions} />
              </Form.Item>
              <Form.Item wrapperCol={{ span: 24 }}>
                <div style={{textAlign: 'center'}}>
                  <Button type="primary" htmlType="submit">确认</Button>
                  <Button style={{ margin: '0 8px', }} onClick={() => {this.setState({permissionModalVisible: false})}}>取消</Button>
                </div>
              </Form.Item>
            </Form>
          </Modal>
        </>
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
        <div className={this.specifyMenuOptionClassName()} onClick={this.onPermissionClick}>权限</div>
        <div className={this.specifyMenuOptionClassName()} onClick={this.onCompressClick}>压缩</div>
        <div className={'contextMenu--separator'} />
        <div className={this.specifyMenuOptionClassName()} onClick={this.onDeleteClick}>删除</div>
      </div>
    )
  }
}
