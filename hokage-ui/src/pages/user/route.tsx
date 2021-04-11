import { createRoute } from '../../libs'
import Operator from './operator'
import OrdinaryUser from './ordinary'

export default [
    createRoute('/operator', Operator),
    createRoute('/ordinary', OrdinaryUser)
]
