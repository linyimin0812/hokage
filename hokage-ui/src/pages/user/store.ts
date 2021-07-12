import { observable } from 'mobx'
import { ServerVO } from '../../axios/action/server/server-type'
import { ServerAction } from '../../axios/action/server/server-action'
import { message } from 'antd'
import { UserVO } from '../../axios/action/user/user-type'

class Store {
  @observable serverList: ServerVO[] = []
  @observable supervisorList: UserVO[] = []

  fetchHasGrantedServerList = (supervisorId: number) => {
    ServerAction.listSupervisorGrantServer(supervisorId).then(list => {
      this.serverList = list || []
    }).catch(e => message.error(e))
  }

  fetchNotGrantedServerList = (supervisorId: number) => {
    ServerAction.listNotGrantedServer(supervisorId).then(list => {
      this.serverList = list || []
    }).catch(e => message.error(e))
  }

}

export default new Store()
