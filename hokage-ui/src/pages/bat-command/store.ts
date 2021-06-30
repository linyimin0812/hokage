import { observable } from 'mobx'
import { BatCommandOperateForm, BatCommandVO, TaskResultVO } from '../../axios/action/bat-command/bat-command-type'
import { ServerVO } from '../../axios/action/server/server-type'
import { FormDataType } from './edit-bat-command'
import { getHokageUid } from '../../libs'
import { BatCommandAction } from '../../axios/action/bat-command/bat-command-action'
import { message } from 'antd'
import moment from 'moment'

interface FormType {
  initCommandFomValue: FormDataType
  isModalVisible: boolean
  isEdit: boolean
}

class Store {
  @observable loading: boolean = false
  @observable records: BatCommandVO[] = []

  @observable serverList: ServerVO[] = []

  @observable taskResultRecords: TaskResultVO[] = []

  @observable form: FormType = {
    initCommandFomValue: {} as FormDataType,
    isModalVisible: false,
    isEdit: false
  }

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
    BatCommandAction.viewTask({id: taskId}).then(commandVO => {
      if (commandVO) {
        this.form = {
          initCommandFomValue: this.assembleInitValue(commandVO),
          isModalVisible: true,
          isEdit: isEdit
        }
      } else {
        message.error('获取任务信息失败')
      }
    }).catch(e => message.error(e))
      .finally(() => this.loading = false)
  }

  assembleInitValue = (commandVO: BatCommandVO) => {
    return {
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
    this.form = {
      initCommandFomValue: {} as FormDataType,
      isModalVisible: true,
      isEdit: true
    }
  }
}

export default new Store()
