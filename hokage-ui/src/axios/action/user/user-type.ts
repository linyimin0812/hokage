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
  key?: string,
  username: string,
  email: string,
  role: number,
  serverNum: number,
  serverGroupList: string[],
  operationList: Operation[],
  serverVOList: ServerVO[],
}

export interface Operation {
  operationType: 'link' | 'confirm' | 'modal' | 'action',
  operationName: string,
  operationLink?: string,
  operationAction?: Function,
  description?: string,
}

export interface UserServerOperateForm {
  operatorId?: number,
  serverIds?: number[],
  userIds?: number[],
  serverGroup?: ServerGroup | string
}

export interface UserServerSearchForm {
  operatorId: number,
  username: string,
  label: string
}

export enum UserRoleEnum {
  super_operator= 100,
  supervisor = 1,
  subordinate = 2
}
