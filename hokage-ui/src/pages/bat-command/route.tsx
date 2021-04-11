import { createRoute } from '../../libs'
import BatCommand from './index'

export default [
    createRoute('/task', BatCommand)
]
