import { observable } from 'mobx'
import { HomeDetailVO } from '../../axios/action/home/home-type'
import { HomeAction } from '../../axios/action/home/home-action'
import { message } from 'antd'

class Store {
  @observable isFetching: boolean = false
  @observable homeDetailVO: HomeDetailVO = {
    allVO: { total: 0, groupInfo: {}},
    availableVO: { total: 0, groupInfo: {}},
    accountVO: { total: 0, groupInfo: {}},
  }

  fetchHomeDetail = () => {
    this.isFetching = true
    HomeAction.homeDetail().then(result => {
      if (!result) {
        return
      }
      this.homeDetailVO = result
    }).catch(e => message.error(e))
      .finally(() => this.isFetching = false)
  }
}

export default new Store()
