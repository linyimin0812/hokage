/**
 * @author linyimin
 * @date 2021/2/17 6:32 pm
 * @email linyimin520812@gmail.com
 * @description
 */
import { Option } from './server-type'
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

    addServerLabel: (form: UserServerOperateForm): Promise<boolean> => {
        return new Promise<boolean>(async (resolve, reject) => {
            try {
                const result: ServiceResult<boolean> = await ServerService.addServerLabel(form)
                if (!result.success) {
                    return reject(result.msg)
                }
                resolve(result.data || false)
            } catch (e) {
                reject('保存服务器分组失败')
            }
        })
    },
    listServerGroup: (id: number): Promise<Option[]> => {
        return new Promise<Option[]>(async (resolve, reject) => {
            try {
                console.log({ id: id })
                const result: ServiceResult<Option[]> = await ServerService.listServerGroup({ id: id })
                if (!result.success) {
                    return reject(result.msg)
                }
                resolve(result.data)
            } catch (e) {
                reject('获取服务器分组失败')
            }
        })
    }

}