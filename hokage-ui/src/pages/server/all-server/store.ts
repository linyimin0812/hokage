import { observable } from 'mobx'
import { ReactText } from 'react'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type'
import { getHokageRole, getHokageUid } from '../../../libs'
import { ServerAction } from '../../../axios/action/server/server-action'

class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable addServerModalVisible: boolean = false

  @observable records: ServerVO[] = []
  @observable isFetching: boolean = false

  fetchRecords = (form: ServerSearchForm = {}) => {
    this.isFetching = true
    form.operatorId = getHokageUid()
    form.role = getHokageRole()
    ServerAction.searchServer(form).then(result => {
      this.records = (result || []).map(serverVO => {
        serverVO.key = serverVO.id + ''
        return serverVO
      })
    }).finally(() => this.isFetching = false)
  }
}

export default new Store()
