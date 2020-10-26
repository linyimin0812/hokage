/**
 * long description for the file
 *
 * @summary short description for the file
 * @author linyimin <linyimin520812@gmail.com>
 *
 * Created at     : 2020-10-27 00:55:23 
 */


import { UserService } from '../../service/index'
import { UserLoginForm, UserLogoutForm, UserRegisterForm } from './user-form'
import { Models } from '../../../utils/model'

 export const UserAction = {
     login: (formData: UserLoginForm): Promise<any> => {
        return new Promise((resolve, reject) => {
            UserService.login(formData).then(value => {
                if (value.success) {
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
                    return resolve(true)
                }
                return reject(value)
            }).catch(err => {
                return reject(err)
            })
        })
     },

     register: (formData: UserRegisterForm): Promise<any> => {
        return new Promise((resolve, reject) => {
            UserService.register(formData).then(value => {
				if (value.success) {
					return resolve(value)
				}
				return reject(value)
			}).catch(err => {
				return reject(err)
			})
        })
     }
 }