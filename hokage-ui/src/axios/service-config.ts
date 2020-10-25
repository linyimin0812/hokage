/**
 * @author linyimin
 * @date 2020/9/5 2:54 下午
 * @email linyimin520812@gmail.com
 * @description 后端接口
 */

import { Method } from 'axios'

export interface ServiceParam {
    url: string,
    method: Method
}

export const ServiceConfig: { [name: string]: ServiceParam} = {
    'register': {
        url: '/user/register',
        method: 'POST'
    },
    'login': {
        url: '/user/login',
        method: 'POST'
    },
    'modifyUserInfo': {
        url: '/user/modify',
        method: 'POST'
    },

    'logout': {
        url: '/user/logout',
        method: 'POST'
    },
}