/**
 * @author linyimin
 * @date 2020/9/5 2:54 下午
 * @email linyimin520812@gmail.com
 * @description 前端访问接口
 */

import axios, { AxiosPromise, AxiosRequestConfig, AxiosResponse } from 'axios';

import { ServiceConfig } from './service-config'

export const Service: {[name: string]: Function} = {}

interface ServiceResult {
    code: string,
    msg?: string,
    data?: any
}

// 将API封装成http访问方法
Object.keys(ServiceConfig).forEach((name: string) => {
    Service[name] = (data?: any, config?: AxiosRequestConfig): AxiosPromise<ServiceResult> => {
        return axios({
            ...ServiceConfig[name],
            ...config,
            data: data
        })
    }
})
