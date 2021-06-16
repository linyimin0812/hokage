import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: {[name: string]: ServiceParam} = {
  'basic': {
    url: '/server/monitor/basic',
    method: 'POST'
  }
}

export const MonitorService = serviceConfig(serviceInfo)
