import { observable } from 'mobx'
import { ReactText } from 'react'
import { ServerSearchForm, ServerVO } from '../../axios/action/server/server-type';
import { getHokageRole, getHokageUid } from '../../libs';
import { ServerAction } from '../../axios/action/server/server-action';
import { message } from 'antd';

class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isFetching: boolean = false
  @observable records: ServerVO[] = []

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
