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
  remove: (form: FileOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await FileManagementService.remove(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('删除文件失败')
      }
    })
  },
  tar: (form: FileOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await FileManagementService.tar(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('打包文件失败')
      }
    })
  },
  move: (form: FileOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await FileManagementService.move(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('修改文件失败')
      }
    })
  },
  chmod: (form: FileOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await FileManagementService.chmod(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data!)
      } catch (err) {
        reject('修改文件权限失败')
      }
    })
  },
}
