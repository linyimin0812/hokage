import { FileManagementService } from '../../service/file-management-service'
import { ServiceResult } from '../../common'
import { FileContentVO, FileOperateForm, FileVO } from './file-management-type'


export const FileManagementAction ={
  list: (form: FileOperateForm): Promise<FileVO> => {
    return new Promise<FileVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<FileVO> = await FileManagementService.list(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取文件信息失败')
      }
    })
  },

  open: (form: FileOperateForm): Promise<FileContentVO> => {
    return new Promise<FileContentVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<FileContentVO> = await FileManagementService.open(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('获取文件内容失败')
      }
    })
  },
}
