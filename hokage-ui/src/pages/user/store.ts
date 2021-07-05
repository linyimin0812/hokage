import { observable } from 'mobx';
import { ServerVO } from '../../axios/action/server/server-type';
import { ServerAction } from '../../axios/action/server/server-action';
import { message } from 'antd';

class Store {
  @observable grantedServerList: ServerVO[] = []


  fetchHasGrantedServerList = (supervisorId: number) => {
    ServerAction.listSupervisorGrantServer(supervisorId).then(list => {
      this.grantedServerList = list || []
    }).catch(e => message.error(e))
  }

  fetchNotGrantedServerList = (supervisorId: number) => {
    ServerAction.listNotGrantedServer(supervisorId).then(list => {
      this.grantedServerList = list || []
    }).catch(e => message.error(e))
  }

}

export default new Store()
