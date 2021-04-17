import { observable } from 'mobx'
import { ReactText } from 'react'
import { Option } from '../../../axios/action/server/server-type'
import { UserAction } from '../../../axios/action'
import { UserVO } from '../../../axios/action/user/user-type';
import { UserSearchFormType } from '../search';

export class Store {
  @observable selectedRowKeys: ReactText[] = []
  @observable isModalVisible: boolean = false

  // add operator
  @observable userOptions: Option[] = []
  @observable isFetching: boolean = false

  @observable records: UserVO[] = []

  /**
   * 获取普通用户列表
   */
  fetchUserOptions = () => {
    this.isFetching = true
    UserAction.listAllSubordinate().then(userVOList => {
      const subordinateUserOptions: Option[] = userVOList.map(userVO => {
        return { label: `${userVO.username}(${userVO.email})`, value: userVO.id }
      })
      this.userOptions = subordinateUserOptions
    }).finally(() => this.isFetching = false)
  }

  /**
   * 获取管理员表格数据
   * @param value
   */
  fetchRecords = (value?: UserSearchFormType) => {
    this.isFetching = true
    UserAction.supervisorSearch(value ? value : {}).then(supervisorList => {
      supervisorList = (supervisorList || []).map(userVO => {
        userVO.key = userVO.id + ''
        return userVO
      })
      this.records = supervisorList
    }).finally(() => { this.isFetching = false })
  }

}

export default new Store()
