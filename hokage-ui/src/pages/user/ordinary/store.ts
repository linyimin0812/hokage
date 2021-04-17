import { observable } from 'mobx'
import { ReactText } from 'react'
import { UserVO } from '../../../axios/action/user/user-type'
import { UserAction } from '../../../axios/action'
import { UserSearchFormType } from '../common/search'

export class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isFetching: boolean = false
  @observable records: UserVO[] = []

  fetchRecords = (value: UserSearchFormType) => {
    this.isFetching = true
    UserAction.searchSubOrdinate(value).then(userList => {
      this.records = userList

    }).finally(() => this.isFetching = false)
  }
}

export default new Store()
