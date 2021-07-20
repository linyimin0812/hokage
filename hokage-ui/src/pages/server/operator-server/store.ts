import { observable } from 'mobx'
import { ReactText } from 'react'
import { ServerSearchForm, ServerUserVO, ServerVO } from '../../../axios/action/server/server-type'
import { getHokageRole, getHokageUid } from '../../../libs'
import { ServerAction } from '../../../axios/action/server/server-action'
import { message } from 'antd';

class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isModalVisible: boolean = false

  @observable records: ServerVO[] = []
  @observable isFetching: boolean = false

  @observable accountList: ServerUserVO[] = []

  fetchRecords = (form: ServerSearchForm = {}) => {
    form.operatorId = getHokageUid()
    form.role = getHokageRole()
    this.isFetching = true
    ServerAction.searchSupervisorServer(form).then(result => {
      this.records = (result || []).map(serverVO => {
        serverVO.key = serverVO.id + ''
        return serverVO
      })
    }).finally(() => this.isFetching = false)
  }

  fetchAccountList = (serverId: number) => {
    this.isFetching = true
    ServerAction.listAccount(serverId).then(accountList => {
      this.accountList = accountList ? accountList : []
    }).catch(e => message.error(e))
      .finally(() => this.isFetching = false)
  }
}

export default new Store()
