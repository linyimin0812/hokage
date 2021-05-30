import { observable } from 'mobx'
import { ReactText } from 'react'
import { ServerSearchForm, ServerVO } from '../../axios/action/server/server-type'
import { getHokageRole, getHokageUid } from '../../libs'
import { ServerAction } from '../../axios/action/server/server-action'
import { message } from 'antd'
import { Terminal } from 'xterm'

export interface PanesType {
  title: string | JSX.Element,
  content: JSX.Element,
  key: string,
  terminal?: Terminal,
  closable?: boolean,
  status?: number, // 0-连接中, 1-已连接， 2-断开连接
}

class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isFetching: boolean = false
  @observable records: ServerVO[] = []
  @observable panes: PanesType[] = []
  @observable activeKey: string = '1'

  fetchRecords = () => {
    this.isFetching = true
    const form: ServerSearchForm = {
      operatorId: getHokageUid(),
      role: getHokageRole()
    }
    ServerAction.searchServer(form).then(result => {
      result = (result || []).map(serverVO => {
        serverVO.key = serverVO.id + ''
        return serverVO
      })
      this.records = result
    }).catch(err => message.error(err)).finally(() => this.isFetching = false)
  }
}

export default new Store()
