import { createRoute } from '../../libs'
import AllServer from './all-server'
import MyServer from './my-server'
import MyOperatorServer from './my-operator-server/operator-server'


export default [
    createRoute('/all', AllServer),
    createRoute('/operator', MyOperatorServer),
    createRoute('/my', MyServer)
]
