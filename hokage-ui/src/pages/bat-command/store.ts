import { observable } from 'mobx';
import { BatCommandVO } from '../../axios/action/bat-command/bat-command-type';

class Store {
  @observable loading: boolean = false
  @observable records: BatCommandVO[] = []
}

export default new Store()
