/**
 * @author linyimin
 * @date 2021/2/17 8:13 pm
 * @email linyimin520812@gmail.com
 * @description
 */
import { Operation } from '../user/user-type'

export interface Option {
    label: string,
    value: any
}

export interface ServerVO {
    id: number,
    hostname: string,
    domain: string,
    ip: string,
    loginType: string,
    sshPort: string,
    serverGroup: string[],
    description: string,
    labels: string[],
    supervisorList: string[],
    supervisorIdList: number[],
    subordinateList: string[],
    subordinateIdList: number[],
    userNum: number,
    status: string,
    operationList: Operation[],
}

export interface ServerGroup {
    id?: number,
    name: string,
    description: string,
    creatorId: number,
}