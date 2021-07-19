import { observable } from 'mobx'
import { HomeDetailVO } from '../../axios/action/home/home-type'
import { HomeAction } from '../../axios/action/home/home-action'
import { message } from 'antd'
import { MetricVO } from '../../axios/action/monitor/monitor-type'

class Store {
  @observable isFetching: boolean = false
  @observable homeDetailVO: HomeDetailVO = {
    allVO: { total: 0, groupInfo: {}},
    availableVO: { total: 0, groupInfo: {}},
    accountVO: { total: 0, groupInfo: {}},
  }

  @observable systemMetric: MetricVO = {
    loadAvgMetric: [{ time: '', value: 0, category: '' }],
    cpuStatMetric: [{ time: '', value: 0, category: '' }],
    memStatMetric: [{ time: '', value: 0, category: '' }],
    uploadStatMetric: [{ time: '', value: 0, category: '' }],
    downloadStatMetric: [{ time: '', value: 0, category: '' }]
  }

  @observable serverIp: string = ''
  @observable serverId: number = 0
  @observable isMetricFetching: boolean = false

  fetchHomeDetail = () => {
    this.isFetching = true
    HomeAction.homeDetail().then(result => {
      if (!result) {
        return
      }
      this.homeDetailVO = result
    }).catch(e => message.error(e))
      .finally(() => this.isFetching = false)
  }

  fetchHomeSystemMetric = () => {
    this.isMetricFetching = true
    HomeAction.homeSystemMetric().then(metric => {
      if (!metric) {
        return
      }
      this.systemMetric = metric.metricVO
      this.serverIp = metric.serverIp
    }).catch(e => message.error(e))
      .finally(() => this.isMetricFetching = false)
  }
}

export default new Store()
