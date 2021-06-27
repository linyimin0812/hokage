import { observable } from 'mobx';
import { BatCommandVO } from '../../axios/action/bat-command/bat-command-type';
import { ServerVO } from '../../axios/action/server/server-type';

class Store {
  @observable loading: boolean = false
  @observable records: BatCommandVO[] = []

  @observable serverList: ServerVO[] = []

  fetchServerList = () => {

  }

}

export default new Store()
