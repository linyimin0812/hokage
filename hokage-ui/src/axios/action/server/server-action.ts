/**
 * @author linyimin
 * @date 2021/2/17 6:32 pm
 * @email linyimin520812@gmail.com
 * @description
 */
import { Option } from './server-type'
import { ServerService } from '../../service/server-service'
import { ServiceResult } from '../../common'

export const ServerAction = {
    listServerOptions: (): Promise<Option[]> => {
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
}