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

  @observable basicLoading: boolean = false

  @observable metricLoading: boolean = false

  // toolbar
  @observable autoRefresh: boolean = false
  @observable start: number = 0
  @observable end: number = 0
  @observable interval: NodeJS.Timeout | null = null
  @observable timestamp: number = 0
  @observable restSeconds: number = 0
  @observable timeType: string = '10min'

}

export default new Store()
