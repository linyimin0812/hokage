import { ServerVO } from '../../axios/action/server/server-type'
import { observable } from 'mobx'
import { MetricMetaVO, MetricVO, MonitorOperateForm } from '../../axios/action/monitor/monitor-type';
import { MonitorAction } from '../../axios/action/monitor/monitor-action'
import { message } from 'antd'

export interface MonitorPanesType {
  content: JSX.Element | null,
  title: string,
  key: string,
  serverVO?: ServerVO,
  closable?: boolean,
}

const defaultMetaMetric: MetricMetaVO = {
  timeList: [],
  series: []
}

const defaultMetric: MetricVO = {
  loadAvgMetric: defaultMetaMetric,
  cpuStatMetric: defaultMetaMetric,
  memStatMetric: defaultMetaMetric,
  uploadStatMetric: defaultMetaMetric,
  downloadStatMetric: defaultMetaMetric,
}

class Store {
  @observable panes: MonitorPanesType[] = []
  @observable activeKey: string = '1'

  @observable loading: boolean = false

  @observable metric: MetricVO = defaultMetric

  acquireSystemStat = (form: MonitorOperateForm) => {
    this.loading = true
    MonitorAction.metric(form).then(metric => {
      if (!metric) {
        this.metric = defaultMetric
        return
      }

      ['cpuStatMetric', 'memStatMetric', 'loadAvgMetric', 'downloadStatMetric', 'uploadStatMetric'].forEach(field => {
        // @ts-ignore
        metric[field].series.forEach(metric => {
          metric.type = 'line'
          metric.smooth = true
        })
      })
      this.metric = metric

    }).catch(e => message.error(e))
      .finally(() => this.loading = false)
  }

}

export default new Store()
