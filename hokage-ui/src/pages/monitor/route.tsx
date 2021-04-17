import { createRoute } from '../../libs'
import ServerResourceManagementHome from './index'

export default [
  createRoute('/monitor', ServerResourceManagementHome)
]
