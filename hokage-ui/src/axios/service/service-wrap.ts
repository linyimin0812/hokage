/**
 * @author linyimin
 * @date 2020/9/5 2:54 下午
 * @email linyimin520812@gmail.com
 * @description 前端访问接口
 */

import axios, { AxiosPromise, AxiosRequestConfig } from 'axios'
import { ServiceParam, ServiceResult } from './common'

export const serviceConfig = (serviceInfo: {[name: string]: ServiceParam}) => {
    const service: {[name: string]: (data?: any, config?: AxiosRequestConfig)=> Promise<ServiceResult>} = {}
    // // 将API封装成http访问方法
    Object.keys(serviceInfo).forEach((name: string) => {
        service[name] = (data?: any, config?: AxiosRequestConfig): Promise<ServiceResult> => {
            return new Promise<ServiceResult>((resolve, reject) => {
                const promise: AxiosPromise<ServiceResult> = axios({
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
