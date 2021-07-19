import { HomeDetailVO } from './home-type'
import { ServiceResult } from '../../common'
import { HomeService } from '../../service/home-service'

export const HomeAction = {
  homeDetail: (): Promise<HomeDetailVO> => {
    return new Promise<HomeDetailVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<HomeDetailVO> = await HomeService.homeDetail()
        if (result.success) {
          return resolve(result.data)
        }
        return reject(`code: ${result.code}, msg: ${result.msg}`)
      } catch (e) {
        reject('获取首页信息失败')
      }
    })
  }
}
