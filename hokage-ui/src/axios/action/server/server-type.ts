/**
 * @author linyimin
 * @date 2021/2/17 8:13 pm
 * @email linyimin520812@gmail.com
 * @description
 */
import { Operation } from '../user/user-type'
import { UploadChangeParam } from 'antd/lib/upload/interface'

export interface Option {
    label: string,
    value: any
}

export interface ServerGroupOption extends Option {
    id?: number
}

export interface ServerVO {
    id: number,
    key?: string,
    hostname: string,
    domain: string,
    ip: string,
    loginType: string,
    account: string,
    sshPort: string,
    serverGroupList: string[],
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
    description?: string,
    creatorId?: number,
}

export interface ServerForm {
    id: number,
    domain: string,
    ip: string,
    loginType: number,
    sshPort: string,
    serverGroupList: string[],
    description: string,
    labels: string[],
    supervisors: number[],
    operatorId: number,
    passwd: string | UploadChangeParam
}

export interface ServerSearchForm extends ServerSearchBaseForm{
    supervisorName?: string,
    serverGroup?: string,
    status?: string,
    account?: string,
    accountStatus?: string,
    username?: string
}

interface ServerSearchBaseForm {
    operatorId?: number,
    role?: number,
    hostname?: string,
    domain?: string,
    ip?: string,
    serverGroup?: string,
}