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

  @observable basicLoading: boolean = false

  @observable metricLoading: boolean = false

  @observable metric: MetricVO = defaultMetric

  // toolbar
  @observable autoRefresh: boolean = false
  @observable start: number = 0
  @observable end: number = 0
  @observable interval: NodeJS.Timeout | null = null
  @observable timestamp: number = 0
  @observable restSeconds: number = 0
  @observable timeType: string = '10min'

  acquireSystemStat = (form: MonitorOperateForm) => {
    this.metricLoading = true
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
      .finally(() => this.metricLoading = false)
  }

}

export default new Store()
