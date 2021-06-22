import { BasicInfoVO, MetricVO, MonitorOperateForm, NetworkInfoVO, SystemInfoVO } from './monitor-type';
import { ServiceResult } from '../../common'
import { MonitorService } from '../../service/monitor-service'

export const MonitorAction = {
  basic: (form: MonitorOperateForm): Promise<BasicInfoVO> => {
    return new Promise<BasicInfoVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<BasicInfoVO> = await MonitorService.basic(form)
        if (!result.success) {
          return reject(`${result.code} ${result.msg}`)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器基本信息失败')
      }
    })
  },
  system: (form: MonitorOperateForm): Promise<SystemInfoVO> => {
    return new Promise<SystemInfoVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<SystemInfoVO> = await MonitorService.system(form)
        if (!result.success) {
          return reject(`${result.code} ${result.msg}`)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器信息失败')
      }
    })
  },
  killProcess: (form: MonitorOperateForm): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
      try {
        const result: ServiceResult<boolean> = await MonitorService.killProcess(form)
        if (!result.success) {
          return reject(`${result.code} ${result.msg}`)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取服务器信息失败')
      }
    })
  },
  networkBasic: (form: MonitorOperateForm): Promise<NetworkInfoVO> => {
    return new Promise<NetworkInfoVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<NetworkInfoVO> = await MonitorService.networkBasic(form)
        if (!result.success) {
          return reject(`${result.code} ${result.msg}`)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取网络信息失败')
      }
    })
  },
  metric: (form: MonitorOperateForm): Promise<MetricVO> => {
    return new Promise<MetricVO>(async (resolve, reject) => {
      try {
        const result: ServiceResult<MetricVO> = await MonitorService.metric(form)
        if (!result.success) {
          return reject(`${result.code} ${result.msg}`)
        }
        resolve(result.data)
      } catch (e) {
        reject('获取系统信息失败')
      }
    })
  },
}
