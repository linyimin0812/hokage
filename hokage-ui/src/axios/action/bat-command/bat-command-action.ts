import { ServiceResult } from '../../common'
import { BatCommandOperateForm, FixedDateTaskForm, BatCommandVO } from './bat-command-type'
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
}
