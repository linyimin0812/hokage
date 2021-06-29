import { observable } from 'mobx'
import { BatCommandOperateForm, BatCommandVO, TaskResultVO } from '../../axios/action/bat-command/bat-command-type'
import { ServerVO } from '../../axios/action/server/server-type'
import { FormDataType } from './edit-bat-command'
import { getHokageUid } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import { message } from 'antd'
import moment from 'moment'

class Store {
  @observable loading: boolean = false
  @observable records: BatCommandVO[] = []

  @observable serverList: ServerVO[] = []

  @observable taskResultRecords: TaskResultVO[] = []

  @observable initCommandFomValue: FormDataType = {} as FormDataType

  @observable isModalVisible: boolean = false
  @observable isEdit: boolean = false

  searchTaskList = () => {
    this.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid()
    }
    BatCommandAction.search(form).then(list => {
      this.records = list ? list : []
    }).catch(e => message.error(e)).finally(() => this.loading = false)
  }

  searchTaskResult = () => {
    this.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid()
    }
    BatCommandAction.listTaskResult(form).then(list => {
      this.taskResultRecords = list ? list : []
    }).catch(e => message.error(e)).finally(() => this.loading = false)
  }

  editTask = (taskId: number, isEdit: boolean) => {
    this.loading = true
    const form: BatCommandOperateForm = {
      operatorId: getHokageUid(),
      taskId: taskId
    }
    BatCommandAction.search(form).then(list => {
      if (!list || list.length === 0) {
        return
      }
      this.assembleInitValue(list[0])
      this.isEdit = isEdit
      this.isModalVisible = true
    }).catch(e => message.error(e)).finally(() => this.loading = false)
  }

  assembleInitValue = (commandVO: BatCommandVO) => {
    this.initCommandFomValue = {
      id: commandVO.id,
      taskName: commandVO.taskName,
      taskType: commandVO.taskType,
      execType: commandVO.execType,
      execTime: moment(commandVO.execTime),
      execServers: commandVO.execServers,
      execCommand: commandVO.execCommand
    }
  }

  createTask = () => {
    this.initCommandFomValue = {} as FormDataType
    this.isEdit = true
    this.isModalVisible = true
  }
}

export default new Store()
