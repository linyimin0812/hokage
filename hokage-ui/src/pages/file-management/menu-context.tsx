import React from 'react'
import { FileProperty } from '../../axios/action/file-management/file-management-type'
import FileTable from './table'
import { message, Modal } from 'antd'
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
@observer
export default class MenuContext extends React.Component<ActionProps> {

  onOpenClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('打开失败')
    }
    store.actionProps = { visible: false }
    fileTable!.onDoubleClick(record!)
  }

  onDownloadClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('下载失败')
    }
    store.actionProps = { visible: false }
    fileTable!.downloadFile(record!)
  }

  onDeleteClick = () => {
    const { record, fileTable } = this.props
    if (!fileTable || !record) {
      message.error('删除失败')
    }
    store.actionProps = { visible: false }
    const type = record!.type === 'file' ? '文件' : '文件夹'
    Modal.confirm({
      onOk: () => { fileTable!.removeFile(record!) },
      okText: 'confirm',
      cancelText: 'cancel',
      content: `确定删除${type}: ${path.resolve(record?.curDir!, record?.name!)}`,
      title: <span><DeleteFilled translate style={{color: 'red'}} /></span>
    })
  }

  render() {
    const { visible, left, top } = this.props
    if (!visible) {
      return null
    }
    return (
      <div className={'contextMenu'} style={{ left: left, top: top }}>
        <div className={'contextMenu--option'} onClick={this.onOpenClick}>打开</div>
        <div className={'contextMenu--option'} onClick={this.onDownloadClick}>下载</div>
        <div className={'contextMenu--separator'} />
        <div className={'contextMenu--option contextMenu--option__disabled'}>重命名</div>
        <div className={'contextMenu-separator'} />
        <div className={'contextMenu--option contextMenu--option__disabled'}>分享</div>
        <div className={'contextMenu--separator'} />
        <div className={'contextMenu--option contextMenu--option__disabled'}>权限</div>
        <div className={'contextMenu--option contextMenu--option__disabled'}>压缩</div>
        <div className={'contextMenu--separator'} />
        <div className={'contextMenu--option'} onClick={this.onDeleteClick}>删除</div>
      </div>
    )
  }
}
