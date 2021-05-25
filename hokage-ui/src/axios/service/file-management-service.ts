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
  'listServerLabel': {
    url: '/server/file/list',
    method: 'POST'
  },
}

export const FileManagementService = serviceConfig(serviceInfo)
