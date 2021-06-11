import React from 'react'
import { message, Result, Table } from 'antd'
import './index.less'
import MenuContext from './menu-context'
import { FileOperation } from './file-operation'
import { observer } from 'mobx-react'
import store from './store'
import { ServerVO } from '../../axios/action/server/server-type'
import { FileContentVO, FileOperateForm, FileProperty } from '../../axios/action/file-management/file-management-type'
import { getHokageUid } from '../../libs'
import {
  DeleteFilled,
  DownloadOutlined,
  EyeFilled,
  FileTextOutlined,
  FolderOpenFilled,
  FolderOutlined,
} from '@ant-design/icons'
import { FileReader } from './file-reader'
import { FileManagementAction } from '../../axios/action/file-management/file-management-action'
import { Action } from '../../component/Action'
import path from 'path'

type FileTablePropsType = {
  id: string,
  serverVO: ServerVO
}

type FileTableStateType = {
  fileReaderVisible: boolean,
  contentVO: FileContentVO
}

@observer
export default class FileTable extends React.Component<FileTablePropsType, FileTableStateType> {

  state = {
    fileReaderVisible: false,
    contentVO: {} as FileContentVO
  }

  componentWillMount = () => {
    // 左键按下时
    window.addEventListener("click", this.onClick)
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

  componentWillUnmount() {
    window.removeEventListener("click", this.onClick)
  }

  onClick = (event: any) => {
    if (!event) {
      return
    }
    const className = JSON.stringify(event.target.className)
    if (!className.includes('contextMenu--option')) {
      store.actionProps = { visible: false }
    }

  }

  getActionPosition = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    // 触点相对浏览器可视区域的左上角距离
    const clickX = event.clientX
    const clickY = event.clientY

    // 当前窗口的高度+宽度
    const screenH = window.innerHeight
    const screenW = window.innerWidth

    // 菜单栏高度+宽度
    const contextW = 100
    const contextH = 205

    // right为true, 说明鼠标点击的位置到浏览器的右边界的宽度可以放下菜单，否则菜单放到左边
    // bottom为true, 说明鼠标点击位置到浏览器的下边界的高度可以放下菜单，否则菜单放在上边
    const right = (screenW - clickX) > contextW
    const bottom = (screenH - clickY) > contextH

    const left = right ? `${clickX}px` : `${clickX - contextW}px`
    const top = bottom ? `${clickY}px` : `${clickY - contextH}px`

    return { visible: true, top: top, left: left }
  }

  onDoubleClick = (record: FileProperty) => {
    const { id } = this.props
    const form: FileOperateForm = this.assembleFileOperateForm(record)
    if (record.type === 'directory') {
      store.listDir(id, form)
    } else {
      this.openFile(form)
    }
  }

  onContextMenu = (record: FileProperty, event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.preventDefault()
    const { visible, left, top } = this.getActionPosition(event)
    store.actionProps = { left, top, record, visible, fileTable: this }
  }

  openFile = (form: FileOperateForm): void => {
    store.loading = true
    FileManagementAction.open(form).then(contentVO => {
      this.setState({ fileReaderVisible: true, contentVO: contentVO })
    }).catch(e => {
      message.error(e)
    }).finally(() => store.loading = false)
  }

  assembleFileOperateForm = (record: FileProperty): FileOperateForm => {
    const { serverVO } = this.props
    const { name, curDir } = record
    return {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      curDir: path.resolve(curDir, name)
    }
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

  renderAction = (record: FileProperty) => {
    const type = record.type === 'file' ? '文件' : '文件夹'
    if (['.', '..'].includes(record.name)) {
      return <Action.Request title={<span><FolderOpenFilled translate /></span>} action={() => this.onDoubleClick(record)} />
    }
    return (
      <Action>
        {this.renderSpecifyAction(record)}
        <Action.Request title={<span><DownloadOutlined translate /></span>} action={() => {this.downloadFile(record)}} />
        <Action.Confirm
          title={<span><DeleteFilled translate style={{color: 'red'}} /></span>}
          action={() => this.removeFile(record)}
          content={`确定删除${type}: ${path.resolve(record.curDir, record.name)}`}
        />
      </Action>
    )
  }

  renderSpecifyAction = (record: FileProperty) => {
    if (record.type === 'file') {
      return <Action.Request title={<span><EyeFilled translate /></span>} action={() => this.onDoubleClick(record)} />
    }
    return <Action.Request title={<span><FolderOpenFilled translate /></span>} action={() => this.onDoubleClick(record)} />
  }

  removeFile = (record: FileProperty) => {
    const { curDir, name } = record
    const type = record.type === 'file' ? '文件' : '文件夹'
    store.loading = true
    let form = this.assembleFileOperateForm(record)
    FileManagementAction.remove(form).then(result => {
      if (result) {
        message.info(`${type}: ${path.resolve(curDir, name)}已删除`)
        const cloneRecord = Object.assign({}, record)
        cloneRecord.name = ''
        form = this.assembleFileOperateForm(cloneRecord)
        store.listDir(this.props.id, form)
      } else {
        message.error(`${type}: ${path.resolve(curDir, name)}删除失败`)
      }
    }).catch(e => {
      message.error(`${type}: ${path.resolve(curDir, name)}删除失败, err: ${e}`)
    }).finally(() => store.loading = false)
  }

  downloadFile = (record: FileProperty) => {
    const { id } = this.props.serverVO

    if (record.type === 'directory') {
      this.downloadDirectory(record)
      return
    }

    const file = path.resolve(record.curDir, record.name)
    const link = document.createElement('a')
    link.href = `/api/server/file/download?id=${id}&file=${file}`
    document.body.appendChild(link)
    const evt = document.createEvent("MouseEvents")
    evt.initEvent("click", false, false)
    link.dispatchEvent(evt)
    document.body.removeChild(link)
    message.warning('即将开始下载，请勿重复点击。')
  }

  downloadDirectory = (record: FileProperty) => {
    const form = this.assembleFileOperateForm(record)
    FileManagementAction.tar(form).then(result => {
      if (result) {
        const cloneRecord = Object.assign({}, record)
        cloneRecord.name = `${record.name}.tar.gz`
        cloneRecord.type = 'file'
        this.downloadFile(cloneRecord)
      } else {
        message.error(`打包文件夹${form.curDir}失败`)
      }
    }).catch(e => message.error(`打包文件夹${form.curDir}失败. err: ` + e))
  }

  render() {
    const { id, serverVO } = this.props
    const { contentVO, fileReaderVisible } = this.state
    const pane = store.panes.find(pane => pane.key === id)
    if (!pane || pane.listDirFailed) {
      return <Result status={'500'} title={'500'} subTitle={`无法获取文件信息，请检查服务器${serverVO.account}@${serverVO.ip}是否可用`} />
    }
    const fileVO = pane.fileVO!
    return (
      <div>
        <MenuContext {...store.actionProps} />
        <FileReader visible={fileReaderVisible} contentVO={contentVO} close={() => {this.setState({ fileReaderVisible: false })}} />
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
          <Table.Column title={'操作'} render={this.renderAction} />
        </Table>
      </div>
    )
  }
}
