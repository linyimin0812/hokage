import { createRoute } from '../../libs'
import AllServer from './all-server'
import MyServer from './my-server'
import MyOperatorServer from './operator-server'


export default [
  createRoute('/all', AllServer),
  createRoute('/operator', MyOperatorServer),
  createRoute('/my', MyServer)
]
