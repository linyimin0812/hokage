import { observable } from 'mobx'
import { ActionProps } from './menu-context'
import { ActionPanesType } from '../common/server-card'
import { FileOperateForm, FileProperty } from '../../axios/action/file-management/file-management-type'
import { FileManagementAction } from '../../axios/action/file-management/file-management-action'
import { message } from 'antd';

class Store {
  @observable currentDir: string = ''
  @observable isActionVisible: boolean = false
  @observable actionProps: ActionProps = {
    left: undefined,
    top: undefined,
    record: undefined
  }

  @observable actionPanes: ActionPanesType[] = []
  @observable activeKey: string = ''

  // file information
  @observable records: FileProperty[] = []
  @observable loading: boolean = false
  @observable fileNum: number = 0;
  @observable directoryNum: number = 0;
  @observable totalSize: string = ''

  listDir = (form: FileOperateForm) => {
    this.loading = true
    FileManagementAction.list(form).then(fileVO => {
      this.records = fileVO.filePropertyList || []
      this.currentDir = fileVO.curDir
      this.directoryNum = fileVO.directoryNum
      this.fileNum = fileVO.fileNum
      this.totalSize = fileVO.totalSize
    }).catch(e => message.error(e))
      .finally(() => this.loading = false)
  }
}

export default new Store()
