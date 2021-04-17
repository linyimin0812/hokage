import { createRoute } from '../../libs'
import FileManagementHome from './index'

export default [
  createRoute('/file', FileManagementHome)
]
