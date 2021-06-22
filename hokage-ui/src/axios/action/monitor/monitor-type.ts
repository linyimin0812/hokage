import { GeneralInfoVO } from '../../../pages/monitor/basic-info/basic-info'
import { AccountInfoVO } from '../../../pages/monitor/basic-info/account-info'
import { LastLogInfoVO } from '../../../pages/monitor/basic-info/login-account-info'
import { ProcessInfoVO } from '../../../pages/monitor/system-status/process'
import { DiskInfoVO } from '../../../pages/monitor/system-status/disk-partition'
import { InterfaceIpInfoVO } from '../../../pages/monitor/network/interface-info'
import { ArpInfoVo } from '../../../pages/monitor/network/arpc-cache-table';
import { ConnectionInfoVO } from '../../../pages/monitor/network/connection-table'

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

export interface NetworkInfoVO {
  interfaceIpInfo: InterfaceIpInfoVO[],
  arpInfo: ArpInfoVo[],
  connectionInfo: ConnectionInfoVO[],
}

export interface MetricMetaVO {
  legendList: string[],
  timeList: string[],
  series: {
    name: string,
    type: string
    stack: string,
    data: number[]
  }[]
}

export interface MetricVO {
  loadAvgMetric: MetricMetaVO,
  cpuStatMetric: MetricMetaVO,
  memStatMetric: MetricMetaVO,

  uploadStatMetric: MetricMetaVO,
  downloadStatMetric: MetricMetaVO
}

export interface MonitorOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string,
  pid?: number
  start?: number,
  end?: number
}

