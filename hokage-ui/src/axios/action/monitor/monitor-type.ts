import { GeneralInfoVO } from '../../../pages/monitor/basic-info/basic-info'
import { AccountInfoVO } from '../../../pages/monitor/basic-info/account-info'
import { LastLogInfoVO } from '../../../pages/monitor/basic-info/login-account-info'

export interface BasicInfoVO {
  cpuInfo: GeneralInfoVO[],
  memInfo: GeneralInfoVO[],
  accountInfo: AccountInfoVO[],
  lastLogInfo: LastLogInfoVO[]
}

export interface MonitorOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string
}

