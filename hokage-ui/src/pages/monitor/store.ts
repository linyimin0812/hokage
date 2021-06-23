import { ServerVO } from '../../axios/action/server/server-type'
import { observable } from 'mobx'
import { MetricMetaVO, MonitorOperateForm } from '../../axios/action/monitor/monitor-type'
import { MonitorAction } from '../../axios/action/monitor/monitor-action'
import { message } from 'antd'

export interface MonitorPanesType {
  content: JSX.Element | null,
  title: string,
  key: string,
  serverVO?: ServerVO,
  closable?: boolean,
}

const defaultMetric: MetricMetaVO = {
  legendList: [],
  timeList: [],
  series: []
}

class Store {
  @observable panes: MonitorPanesType[] = []
  @observable activeKey: string = '1'

  @observable loading: boolean = false

  @observable loadAvgMetric: MetricMetaVO = defaultMetric
  @observable cpuStatMetric: MetricMetaVO = defaultMetric
  @observable memStatMetric: MetricMetaVO = defaultMetric

  @observable uploadStatMetric: MetricMetaVO = defaultMetric
  @observable downloadStatMetric: MetricMetaVO = defaultMetric

  acquireSystemStat = (form: MonitorOperateForm) => {
    this.loading = true
    MonitorAction.metric(form).then(metric => {
      if (!metric) {
        this.cpuStatMetric = defaultMetric
        this.memStatMetric = defaultMetric
        this.loadAvgMetric = defaultMetric

        this.uploadStatMetric = defaultMetric
        this.downloadStatMetric = defaultMetric
        return
      }
      this.cpuStatMetric = metric.cpuStatMetric
      this.memStatMetric = metric.memStatMetric
      this.loadAvgMetric = metric.loadAvgMetric

      this.uploadStatMetric = metric.uploadStatMetric
      this.downloadStatMetric = metric.downloadStatMetric
    }).catch(e => message.error(e))
      .finally(() => this.loading = false)
  }

}

export default new Store()
