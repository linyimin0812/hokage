import { observable } from 'mobx'
import { ReactText } from 'react'

export class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isModalVisible: boolean = false

}

export default new Store()
