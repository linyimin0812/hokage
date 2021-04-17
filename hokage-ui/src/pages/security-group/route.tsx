import { createRoute } from '../../libs'
import SecurityGroupHome from './index'

export default [
  createRoute('/security', SecurityGroupHome)
]
