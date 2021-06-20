import { GeneralInfoVO } from '../../../pages/monitor/basic-info/basic-info'
import { AccountInfoVO } from '../../../pages/monitor/basic-info/account-info'
import { LastLogInfoVO } from '../../../pages/monitor/basic-info/login-account-info'
import { ProcessInfoVO } from '../../../pages/monitor/system-status/process'
import { DiskInfoVO } from '../../../pages/monitor/system-status/disk-partition'

export interface BasicInfoVO {
  cpuInfo: GeneralInfoVO[],
  memInfo: GeneralInfoVO[],
  accountInfo: AccountInfoVO[],
  lastLogInfo: LastLogInfoVO[],
  generalInfo: GeneralInfoVO[],
}

export interface SystemInfoVO {
  processInfo: ProcessInfoVO[],
  diskInfo: DiskInfoVO[],
}

export interface MonitorOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string,
  pid?: number
}

