import { observable } from 'mobx'
import { ServerForm, ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type';
import { getHokageRole, getHokageUid } from '../../../libs'
import { ServerAction } from '../../../axios/action/server/server-action'
import { message } from 'antd';

type ServerFormType = {
  editServerModalVisible: boolean,
  initFormValue: ServerForm | undefined
}

class Store {

  @observable records: ServerVO[] = []
  @observable isFetching: boolean = false

  @observable form: ServerFormType = {
    initFormValue: undefined,
    editServerModalVisible: false
  }

  fetchRecords = (form: ServerSearchForm = {}) => {
    this.isFetching = true
    form.operatorId = getHokageUid()
    form.role = getHokageRole()
    ServerAction.searchAllServer(form).then(result => {
      this.records = (result || []).map(serverVO => {
        serverVO.key = serverVO.id + ''
        return serverVO
      })
    }).finally(() => this.isFetching = false)
  }

  fetchRecord = (serverId: number) => {
    this.isFetching = true
    ServerAction.viewServer(serverId).then(serverVO => {
      if (!serverVO) {
        return
      }
      this.form = {
        editServerModalVisible: true,
        initFormValue: {
          id: serverVO.id,
          domain: serverVO.domain,
          ip: serverVO.ip,
          account: serverVO.account,
          loginType: serverVO.loginType as number,
          sshPort: serverVO.sshPort,
          serverGroupList: serverVO.serverGroupList,
          description: serverVO.description,
          operatorId: getHokageUid(),
          passwd: ''
        }
      }
    }).catch(e => message.error(e))
      .finally(() => this.isFetching = false)
  }
}

export default new Store()
