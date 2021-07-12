import { observable } from 'mobx'
import { ReactText } from 'react'
import { UserVO } from '../../../axios/action/user/user-type'
import { UserAction } from '../../../axios/action'
import { UserSearchFormType } from '../common/search'
import { getHokageUid } from '../../../libs'

export class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isFetching: boolean = false
  @observable records: UserVO[] = []
  @observable supervisorList: UserVO[] = []

  fetchRecords = (value: UserSearchFormType) => {
    this.isFetching = true
    UserAction.searchSubOrdinate(value).then(userList => {
      this.records = userList

    }).finally(() => this.isFetching = false)
  }

  fetchSupervisorList = () => {
    const form: UserSearchFormType = { operatorId: getHokageUid() }
    UserAction.supervisorSearch(form).then(userList => {
      this.supervisorList = userList || []
    })
  }
}

export default new Store()
