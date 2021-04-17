import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

/**
 * @author linyimin
 * @date 2021/2/17 6:16 pm
 * @email linyimin520812@gmail.com
 * @description
 */

const serviceInfo: { [name: string]: ServiceParam} = {
  // list all server label
  'listServerLabel': {
    url: '/server/label/list',
    method: 'GET'
  },

  // add server label
  'addServerLabel': {
    url: '/server/group/add',
    method: 'POST'
  },

  // list all server group
  'listServerGroup': {
    url: '/server/group/list',
    method: 'GET'
  },

  // save server
  'saveServer': {
    url: '/server/add',
    method: 'POST'
  },

  // list server
  'searchServer': {
    url: '/server/search',
    method: 'POST'
  }
}

export const ServerService = serviceConfig(serviceInfo)
