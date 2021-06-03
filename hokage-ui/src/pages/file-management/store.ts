import { observable } from 'mobx'
import { ActionProps } from './menu-context'
import { ActionPanesType } from '../common/server-card'
import { FileOperateForm, FileVO } from '../../axios/action/file-management/file-management-type';
import { FileManagementAction } from '../../axios/action/file-management/file-management-action';
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
  @observable records: FileVO[] = []
  @observable loading: boolean = false
  listDir = (form: FileOperateForm) => {
    this.loading = true
    FileManagementAction.list(form).then(fileList => {
      this.records = fileList || []
    }).catch(e => message.error(e))
      .finally(() => this.loading = false)
  }
}

export default new Store()
