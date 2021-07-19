import { MetricVO } from '../monitor/monitor-type'

export type MetaData = {
  total: number,
  groupInfo: {[key: string]: number}
}

export type HomeDetailVO = {
  allVO: MetaData,
  availableVO: MetaData,
  accountVO: MetaData
}

export type HomeMetricVO = {
  serverIp: string,
  metricVO: MetricVO
}
