import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: {[name: string]: ServiceParam} = {
  homeDetail: {
    url: '/home/detail',
    method: 'GET'
  }
}

export const HomeService = serviceConfig(serviceInfo)
