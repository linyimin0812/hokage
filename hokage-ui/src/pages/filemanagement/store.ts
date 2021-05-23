import { observable } from 'mobx'
import { ActionProps } from './action';
import { ActionPanesType } from '../common/server-card';

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
}

export default new Store()
