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
    }
}