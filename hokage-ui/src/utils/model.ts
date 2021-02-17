/**
 * @author linyimin
 * @date 2020/9/6 3:38 pm
 * @email linyimin520812@gmail.com
 * @description global store, need to guarantee the uniqueness of key yourself
 */

import _ from 'lodash'

interface StoreType {
    [key: string]: any
}

export const Models = {
    _store:  { serverLabelColor: ['magenta', 'red', 'green', 'purple', '#f50'] },

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