import { observable } from 'mobx';

class Store {
  @observable loading: boolean = false
}

export default new Store()
