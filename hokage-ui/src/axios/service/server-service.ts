import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

/**
 * @author linyimin
 * @date 2021/2/17 6:16 pm
 * @email linyimin520812@gmail.com
 * @description
 */

const serviceInfo: { [name: string]: ServiceParam} = {

  // add server group
  'addServerGroup': {
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
    url: '/server/save',
    method: 'POST'
  },

  // list server
  'searchServer': {
    url: '/server/search',
    method: 'POST'
  },
  'searchAllServer': {
    url: '/server/all/search',
    method: 'POST'
  },
  'listSupervisorGrantServer': {
    url: '/supervisor/grant/server/list',
    method: 'GET'
  },
  'listNotGrantedServer': {
    url: '/supervisor/not/grant/server/list',
    method: 'GET'
  },
  'searchSubordinateServer': {
    url: '/server/subordinate/search',
    method: 'POST'
  },
  searchSupervisorServer: {
    url: '/server/supervisor/search',
    method: 'POST'
  },
  deleteServer: {
    url: '/server/delete',
    method: 'POST'
  }
}

export const ServerService = serviceConfig(serviceInfo)
