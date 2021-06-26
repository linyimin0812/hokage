import { ServiceResult } from '../../common'
import { FixedDateTaskForm } from './bat-command-type'
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
}
