/**
 * @author linyimin
 * @date 2020/9/5 2:54 pm
 * @email linyimin520812@gmail.com
 * @description front end request method
 */

import axios, { AxiosPromise, AxiosRequestConfig } from 'axios'
import { ServiceParam, ServiceResult } from '../common'

export const serviceConfig = (serviceInfo: {[name: string]: ServiceParam}) => {
    const service: {[name: string]: (data?: any, config?: AxiosRequestConfig)=> Promise<ServiceResult<any>>} = {}
    // encapsulate the API as an http access method
    Object.keys(serviceInfo).forEach((name: string) => {
        service[name] = (data?: any, config?: AxiosRequestConfig): Promise<ServiceResult<any>> => {
            return new Promise<ServiceResult<any>>((resolve, reject) => {
                const requestConfig: AxiosRequestConfig = { ...serviceInfo[name], ...config }
                if (['GET', 'get'].includes(serviceInfo[name].method)) {
                    requestConfig.params = data
                } else {
                    requestConfig.data = data
                }
                const promise: AxiosPromise<ServiceResult<any>> = axios(requestConfig)

                promise.then(result => {
                    resolve(result.data)
                }).catch(err => {
                    reject(err)
                })
            })
        }
    })
    return service
}
