import React from 'react'
import { Result, Table } from 'antd'
import './index.less'
import MenuContext from './menu-context'
import { FileOperation } from './file-operation'
import { observer } from 'mobx-react'
import store from './store'
import { ServerVO } from '../../axios/action/server/server-type'
import { FileOperateForm, FileProperty } from '../../axios/action/file-management/file-management-type'
import { getHokageUid } from '../../libs'
import { FileTextOutlined, FolderOutlined } from '@ant-design/icons'

type FileTablePropsType = {
  id: string,
  serverVO: ServerVO
}

@observer
export default class FileTable extends React.Component<FileTablePropsType> {

  componentWillMount = () => {
    // 左键按下时
    window.addEventListener("mousedown", this.onMouseDown)
    const { serverVO } = this.props
    const form: FileOperateForm = {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      curDir: '~'
    }
    store.listDir(this.props.id, form)
  }



  onMouseDown = (event: MouseEvent) => {
    if (event.button === 0 && store.actionProps.left !== undefined) {
      store.actionProps = {
        left: undefined,
        top: undefined,
        record: undefined
      }
    }
  }

  getActionPosition = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    const left = event.clientX
    const top = event.clientY
    const ww = document.documentElement.clientWidth
    const wh = document.documentElement.clientHeight

    return {
      top: ((wh - top > 300) ? top : (top - 330)) + 'px',
      left: ((ww - left > 100) ? left : (left - 100)) + 'px'
    }
  }

  onDoubleClick = (record: FileProperty) => {
    // TODO: 文件夹则打开文件夹
    // TODO: 普通文本文件直接打开
    const { serverVO, id } = this.props
    const pane = store.panes.find(pane => pane.key === id)!
    if (record.type === 'directory') {
      const form: FileOperateForm = {
        operatorId: getHokageUid(),
        ip: serverVO.ip,
        sshPort: serverVO.sshPort,
        account: serverVO.account,
        curDir: pane.fileVO!.curDir + '/' + record.name
      }
      store.listDir(id, form)
    } else {
      alert(`open file ${record.name}`)
    }
  }

  onContextMenu = (record: FileProperty, event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.preventDefault()
    const { left, top } = this.getActionPosition(event)
    store.actionProps = { left, top, record }
  }

  renderName = (record: FileProperty) => {
    if (record.type === 'directory') {
      return (
        <div onClick={() => this.onDoubleClick(record)} style={{cursor: 'pointer'}}>
          <FolderOutlined translate style={{color: '#1890ff'}} />
          <span style={{color: '#1890ff', paddingLeft: 5}}>{record.name}</span>
        </div>
      )
    }
    return (
      <React.Fragment>
        <FileTextOutlined translate />
        <span style={{paddingLeft: 5}} >{record.name}</span>
      </React.Fragment>
    )
  }

  render() {
    const { id, serverVO } = this.props
    const pane = store.panes.find(pane => pane.key === id)
    if (!pane || pane.listDirFailed) {
      return <Result status={'500'} title={'500'} subTitle="无法获取文件信息，请检查服务器是否可用" />
    }
    const fileVO = pane.fileVO!
    return (
      <div>
        { store.actionProps.left !== undefined ? <MenuContext {...store.actionProps} /> : null }
        <FileOperation id={id} serverVO={serverVO} fileVO={fileVO} />
        <Table
          style={{ cursor: 'pointer' }}
          dataSource={fileVO.filePropertyList}
          pagination={false}
          loading={store.loading}
          onRow={(record: FileProperty) => {
            return {
              onDoubleClick: _ => this.onDoubleClick(record),
              onContextMenu: event => this.onContextMenu(record, event)
            }
          }}
          scroll={{ y: 'calc(100vh - 350px)' }}
        >
          <Table.Column title={'文件名'} render={this.renderName} />
          <Table.Column title={'大小'} dataIndex={'size'} />
          <Table.Column title={'权限'} dataIndex={'permission'} />
          <Table.Column title={'所有者'} dataIndex={'owner'} />
          <Table.Column title={'修改时间'} dataIndex={'lastAccessTime'} />
        </Table>
      </div>
    )
  }
}
