import { observable } from 'mobx'
import { ActionProps } from './menu-context';
import { ActionPanesType } from '../common/server-card'
import { Record } from './table'

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
  @observable records: Record[] = []

  listDir = (currentDir: string) => {

  }
}

export default new Store()
