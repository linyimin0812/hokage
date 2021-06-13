/**
 * @author linyimin
 * @date 2021/05/24 20:52
 * @email linyimin520812@gmail.com
 * @description file management service
 */

import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: { [name: string]: ServiceParam} = {
  // list all server label
  'list': {
    url: '/server/file/list',
    method: 'POST'
  },
  'open': {
    url: '/server/file/open',
    method: 'POST'
  },
  'remove': {
    url: '/server/file/rm',
    method: 'POST'
  },
  'tar': {
    url: '/server/file/tar',
    method: 'POST'
  },
  'move': {
    url: '/server/file/move',
    method: 'POST'
  }
}

export const FileManagementService = serviceConfig(serviceInfo)
