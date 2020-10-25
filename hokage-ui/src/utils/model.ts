/**
 * @author linyimin
 * @date 2020/9/6 3:38 下午
 * @email linyimin520812@gmail.com
 * @description 全局存储, 需要自己保证key的唯一性
 */

import _ from 'lodash'

interface StoreType {
    [key: string]: any
}

export const Models = {
    _store:  {} as StoreType,

    get: (key: string): any => {
        return _.get(Models._store, key)
    },

    set: (key: string, value: any): void => {
        _.set(Models._store, key, value)
    },

    remove: (key: string | string[]): void => {
        _.assign(Models._store, _.omit(Models._store, key))
    }
}