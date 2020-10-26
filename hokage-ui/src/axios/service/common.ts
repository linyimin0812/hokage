import { Method } from 'axios'

export interface ServiceParam {
    url: string,
    method: Method
}


export interface ServiceResult {
    success: boolean,
    code: string,
    msg?: string,
    data?: any
}