/**
 * @author linyimin
 * @date 2021/2/17 6:32 pm
 * @email linyimin520812@gmail.com
 * @description
 */
import { Option, ServerForm, ServerSearchForm, ServerVO } from './server-type'
import { ServerService } from '../../service/server-service'
import { ServiceResult } from '../../common'
import { UserServerOperateForm } from '../user/user-type'

export const ServerAction = {
  listServerLabelOptions: (): Promise<Option[]> => {
    return new Promise<Option[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<Option[]> = await ServerService.listServerLabel()
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (err) {
        reject('获取服务器类型失败')
      }
    })
  },

  addServerLabel: (form: UserServerOperateForm): Promise<Option[]> => {
    return new Promise<Option[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<Option[]> = await ServerService.addServerLabel(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data || [])
      } catch (e) {
        reject('保存服务器分组失败')
      }
    })
  },
  listServerGroup: (id: number): Promise<Option[]> => {
    return new Promise<Option[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<Option[]> = await ServerService.listServerGroup({ id: id })
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器分组失败')
      }
    })
  },

  saveServer: (form: ServerForm): Promise<ServerForm> => {
    return new Promise<ServerForm>(async (resolve, reject) => {
      try {
        const result: ServiceResult<ServerForm> = await ServerService.saveServer(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (e) {
        reject('保存服务器信息失败: ' + JSON.stringify(e))
      }
    })
  },

  searchServer: (form: ServerSearchForm): Promise<ServerVO[]> => {
    return new Promise<ServerVO[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<ServerVO[]> = await ServerService.searchServer(form)
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (e) {
        reject('搜索服务器信息失败: ' + JSON.stringify(e))
      }
    })
  },
  listSupervisorGrantServer: (supervisorId: number): Promise<ServerVO[]> => {
    return new Promise<ServerVO[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<ServerVO[]> = await ServerService.listSupervisorGrantServer({id: supervisorId})
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器信息失败: ' + JSON.stringify(e))
      }
    })
  },

  listNotGrantedServer: (supervisorId: number): Promise<ServerVO[]> => {
    return new Promise<ServerVO[]>(async (resolve, reject) => {
      try {
        const result: ServiceResult<ServerVO[]> = await ServerService.listNotGrantedServer({id: supervisorId})
        if (!result.success) {
          return reject(result.msg)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器信息失败: ' + JSON.stringify(e))
      }
    })
  },

}
