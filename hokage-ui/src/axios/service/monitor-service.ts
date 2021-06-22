import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: {[name: string]: ServiceParam} = {
  'basic': {
    url: '/server/monitor/basic',
    method: 'POST'
  },
  'system': {
    url: '/server/monitor/system',
    method: 'POST'
  },
  'killProcess': {
    url: '/server/monitor/process/kill',
    method: 'POST'
  },
  'diskPartition': {
    'url': '/server/monitor/disk/partition',
    method: 'POST'
  },
  'networkBasic': {
    url: '/server/monitor/network/basic',
    method: 'POST'
  },
  'metric': {
    url: '/server/monitor/metric',
    method: 'POST'
  }
}

export const MonitorService = serviceConfig(serviceInfo)
