import { observable } from 'mobx'
import { ActionProps } from './menu-context'
import { FileOperateForm, FileVO } from '../../axios/action/file-management/file-management-type'
import { FileManagementAction } from '../../axios/action/file-management/file-management-action'
import { message } from 'antd'

export interface PanesType {
  content: JSX.Element,
  title: string,
  key: string,
  closable?: boolean,
  fileVO?: FileVO,
  listDirFailed?: boolean
}

class Store {

  @observable isActionVisible: boolean = false
  @observable actionProps: ActionProps = { visible: false }

  @observable panes: PanesType[] = []
  @observable activeKey: string = 'my-server'

  @observable loading: boolean = false

  listDir = (id: string, form: FileOperateForm) => {
    this.loading = true
    FileManagementAction.list(form).then(fileVO => {
      this.panes.filter(pane => pane.key === id).forEach(pane => {
        pane.fileVO = fileVO
        pane.listDirFailed = false
      })
    }).catch(e => {
      message.error(e)
      this.panes.filter(pane => pane.key === id).forEach(pane => {
        pane.listDirFailed = true
      })
    }).finally(() => this.loading = false)
  }
}

export default new Store()
