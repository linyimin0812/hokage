/**
 * long description for the file
 * @summary short description for the file
 * @author linyimin <linyimin520812@gmail.com>
 * Created at     : 2020-10-27 00:55:23
 */

import { UserService } from '../../service'
import {
    UserLoginForm,
    UserLogoutForm,
    UserRegisterForm, UserServerOperateForm, UserVO,
} from './user-type';
import { Models } from '../../../utils/model'
import { ServiceResult } from '../../common'

 export const UserAction = {
     login: (formData: UserLoginForm): Promise<ServiceResult<UserRegisterForm>> => {
        return new Promise((resolve, reject) => {
            UserService.login(formData).then(value => {
                if (value.success && value.data) {
                    const data: UserRegisterForm = value.data
                    // @ts-ignore
                    window.hokageUid = data.id
					return resolve(value)
                }
                
                return reject(value)
				
            }).catch(err => {
				return reject(err)
			})
        })
     },

     logout: (formData: UserLogoutForm): Promise<boolean> => {
        return new Promise<boolean>((resolve, reject) => {
            UserService.logout(formData).then(value => {
                if (value.success) {
                    Models.remove('userInfo')
                    // @ts-ignore
                    window.hokageUid = 0
                    return resolve(true)
                }
                return reject(value)
            }).catch(err => {
                return reject(err)
            })
        })
     },

     register: (formData: UserRegisterForm): Promise<ServiceResult<UserRegisterForm>> => {
        return new Promise((resolve, reject) => {
            UserService.register(formData).then(value => {
				if (value.success && value.data) {
                    const data: UserRegisterForm = value.data
                    // @ts-ignore
                    window.hokageUid = data.id
					return resolve(value)
				}
				return reject(value)
			}).catch(err => {
				return reject(err)
			})
        })
     },

     listSupervisor: (): Promise<UserVO[]> => {
         return new Promise<UserVO[]>((resolve, reject) => {
             UserService.listSupervisor().then(value => {
                 if (value.success) {
                     return resolve(value.data)
                 }
                 reject(value.msg)
             }).catch(err => {
                 return reject("获取服务器管理员失败. err: " + JSON.stringify(err))
             })
         })
     },

     listAllSubordinate: (): Promise<UserVO[]> => {
         return new Promise<UserVO[]>((resolve, reject) => {
             UserService.listAllSubordinate().then(value => {
                 if (value.success) {
                     return resolve(value.data)
                 }
                 reject(value.msg)
             }).catch(err => {
                 return reject("获取普通用户失败. err: " + JSON.stringify(err))
             })
         })
     },

     addSupervisor: (form: UserServerOperateForm): Promise<boolean> => {
         return new Promise<boolean>((resolve, reject) => {
             UserService.addSupervisor(form).then(value => {
                 if (value.success) {
                     return resolve(value.data)
                 }
                 reject(value.msg)
             }).catch(err => {
                 return reject("添加管理员失败. err: " + JSON.stringify(err))
             })
         })
     },
 }