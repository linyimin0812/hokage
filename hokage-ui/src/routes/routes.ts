import BatCommandRoutes from '../pages/bat-command/route'
import FileManagementRoutes from '../pages/filemanagement/route'
import HomeRoutes from '../pages/home/route'
import MonitorRoutes from '../pages/monitor/route'
import PenetrationRoutes from '../pages/penetration/route'
import SecurityGroupRoutes from '../pages/security-group/route'
import ServerRoutes from '../pages/server/route'
import UserRoutes from '../pages/user/route'
import WebSshRoutes from '../pages/webssh/route'
import LoginRoute from '../pages/login/route'
import { createModuleRoute } from '../libs'

export default [
  createModuleRoute('/app/web', BatCommandRoutes),
  createModuleRoute('/app/web', FileManagementRoutes),
  createModuleRoute('/app', HomeRoutes),
  createModuleRoute('/app/server', MonitorRoutes),
  createModuleRoute('/app/server/network', PenetrationRoutes),
  createModuleRoute('/app/server', SecurityGroupRoutes),
  createModuleRoute('/app/server', ServerRoutes),
  createModuleRoute('/app/user', UserRoutes),
  createModuleRoute('/app/web', WebSshRoutes),
  createModuleRoute('/app', LoginRoute)
]
