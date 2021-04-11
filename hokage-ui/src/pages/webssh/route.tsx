import { createRoute } from '../../libs'
import WebSshHome from './index'

export default [
    createRoute('/ssh', WebSshHome)
]
