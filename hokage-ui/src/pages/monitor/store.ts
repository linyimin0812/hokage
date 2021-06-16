import { ServerVO } from '../../axios/action/server/server-type'
import { observable } from 'mobx'

export interface MonitorPanesType {
  content: JSX.Element | null,
  title: string,
  key: string,
  serverVO?: ServerVO,
  closable?: boolean,
}

class Store {
  @observable panes: MonitorPanesType[] = []
  @observable activeKey: string = '1'

  @observable loading: boolean = false

}

export default new Store()
