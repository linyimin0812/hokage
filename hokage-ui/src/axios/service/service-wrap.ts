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
            // @ts-ignore
            const hokageUid = window.hokageUid || 0
            if (hokageUid > 0) {
                data = data ? {...data, hokageUid} : { hokageUid }
            }
            return new Promise<ServiceResult<any>>((resolve, reject) => {
                const promise: AxiosPromise<ServiceResult<any>> = axios({
                    ...serviceInfo[name],
                    ...config,
                    data: data
                })

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
