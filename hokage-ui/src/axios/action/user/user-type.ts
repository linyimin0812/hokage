/**
 * @summary user form data type
 * @author linyimin <linyimin520812@gmail.com>
 *
 * Created at     : 2020-10-27 01:08:26
 * Last modified  : 2020-10-27 01:12:01
 */
import { ServerGroup, ServerVO } from '../server/server-type';

export interface UserLoginForm {
    email: string,
    passwd: string
}


export interface UserLogoutForm {
    email: string
}

export interface UserRegisterForm extends UserLoginForm{
    id?: number,
    username: string,
    role: number,
    subscribed: number
}

export interface UserVO {
    id: number,
    username: string,
    email: string,
    role: number,
    serverNum: number,
    serverLabelList: string[],
    operationList: Operation[],
    serverVOList: ServerVO[],
}

export interface Operation {
    operationType: 'link' | 'confirm' | 'modal' | 'action',
    operationName: string,
    operationLink?: string,
    operationAction?: Function
}

export interface UserServerOperateForm {
    id: number,
    serverIds?: number[],
    userIds?: number[],
    serverGroup?: ServerGroup
}

export interface UserServerSearchForm {
    id: number,
    username: string,
    label: string
}