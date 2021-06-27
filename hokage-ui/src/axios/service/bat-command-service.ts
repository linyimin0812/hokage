import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: { [name: string]: ServiceParam} = {
  'save': {
    url: '/server/bat/save',
    method: 'POST'
  },
  'search': {
    url: '/server/bat/search',
    method: 'POST'
  },
  'delete': {
    url: '/server/bat/delete',
    method: 'POST'
  },
  'offline': {
    url: '/server/bat/offline',
    method: 'POST'
  },
  'online': {
    url: '/server/bat/online',
    method: 'POST'
  }
}

export const BatCommandService = serviceConfig(serviceInfo)
