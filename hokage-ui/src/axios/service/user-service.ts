/**
 * @author linyimin
 * @date 2020/9/5 2:54 pm
 * @email linyimin520812@gmail.com
 * @description user service
 */
import { ServiceParam } from '../common'
import { serviceConfig } from './service-wrap'

const serviceInfo: { [name: string]: ServiceParam} = {
    // user register
    'register': {
        url: '/user/register',
        method: 'POST'
    },
    // user login
    'login': {
        url: '/user/login',
        method: 'POST'
    },
    // user info modify
    'modifyUserInfo': {
        url: '/user/modify',
        method: 'POST'
    },
    // user logout
    'logout': {
        url: '/user/logout',
        method: 'POST'
    },
    // search supervisor
    'searchSupervisor': {
        url: '/user/supervisor/search',
        method: 'POST'
    },

    // add supervisor
    'addSupervisor': {
        url: '/user/supervisor/add',
        method: 'POST'
    },

    // delete supervisor
    'deleteSupervisor': {
        url: '/user/supervisor/delete',
        method: 'POST'
    },

    // view supervisor
    'viewSupervisor': {
        url: '/user/supervisor/view',
        method: 'POST'
    },
    // grant supervisor server
    'grantSupervisorServer': {
        url: '/user/supervisor/server/grant',
        method: 'POST'
    },
    // recycle supervisor server
    'recycleSupervisorServer': {
        url: '/user/supervisor/server/recycle',
        method: 'POST'
    },
    // list subordinate
    'listSubordinate': {
        url: '/user/subordinate/list',
        method: 'POST'
    },
    // list all subordinate
    'listAllSubordinate': {
        url: '/user/subordinate/all',
        method: 'GET'
    },
    // search subordinate user
    'searchSubOrdinate': {
        url: '/user/subordinate/search',
        method: 'POST'
    },

    // add subordinate
    'addSubordinate': {
        url: '/user/subordinate/add',
        method: 'POST'
    },
    // delete subordinate
    'deleteSubordinate': {
        url: '/user/subordinate/delete',
        method: 'POST'
    },

    // view subordinate
    'viewSubordinate': {
        url: '/user/subordinate/view',
        method: 'POST'
    },

    // grant subordinate server
    'grantSubordinateServer': {
        url: '/user/subordinate/server/grant',
        method: 'POST'
    },

    // recycle subordinate server
    'recycleSubordinateServer': {
        url: '/user/subordinate/server/recycle',
        method: 'POST'
    },

}

export const UserService = serviceConfig(serviceInfo)