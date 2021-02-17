/**
 * @summary user form data type
 * @author linyimin <linyimin520812@gmail.com>
 *
 * Created at     : 2020-10-27 01:08:26 
 * Last modified  : 2020-10-27 01:12:01
 */

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