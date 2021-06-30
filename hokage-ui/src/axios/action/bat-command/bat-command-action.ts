import { ServiceResult } from '../../common'
import { BatCommandOperateForm, FixedDateTaskForm, BatCommandVO, TaskResultVO, TaskInfoVO } from './bat-command-type'
import { BatCommandService } from '../../service/bat-command-service'

export const BatCommandAction ={
  save: (form: FixedDateTaskForm): Promise<number> => {
    return new Promise<number>(async (resolve, reject) => {
      try {
        const result: ServiceResult<number> = await BatCommandService.save(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('保存批量命令失败')
      }
    })
  },
  search: (form: BatCommandOperateForm): Promise<BatCommandVO[]> => {
    return new Promise<BatCommandVO[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<BatCommandVO[]> = await BatCommandService.search(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取批量任务列表失败')
      }
    })
  },
  delete: (form: BatCommandOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await BatCommandService.delete(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('删除任务失败')
      }
    })
  },
  offline: (form: BatCommandOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await BatCommandService.offline(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('下线任务失败')
      }
    })
  },
  online: (form: BatCommandOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await BatCommandService.online(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('上线任务失败')
      }
    })
  },
  listTaskResult: (form: BatCommandOperateForm): Promise<TaskResultVO[]> => {
    return new Promise<TaskResultVO[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<TaskResultVO[]> = await BatCommandService.listTaskResult(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取任务结果失败')
      }
    })
  },
  executeTask: (form: BatCommandOperateForm): Promise<void> => {
    return new Promise<void>(async (resolve, reject) => {
      try {
        const result: ServiceResult<void> = await BatCommandService.executeTask(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('执行任务失败')
      }
    })
  },
  viewSingleTaskDetail: (form: BatCommandOperateForm): Promise<TaskInfoVO> => {
    return new Promise<TaskInfoVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<TaskInfoVO> = await BatCommandService.viewSingleTaskDetail(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取任务详情失败')
      }
    })
  },
  viewTask: (param: {id: number}): Promise<BatCommandVO> => {
    return new Promise<BatCommandVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<BatCommandVO> = await BatCommandService.viewTask(param)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取批量任务列表失败')
      }
    })
  },
}
