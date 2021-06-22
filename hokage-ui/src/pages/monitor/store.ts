import { ServerVO } from '../../axios/action/server/server-type'
import { observable } from 'mobx'
import { MetricMetaVO, MonitorOperateForm } from '../../axios/action/monitor/monitor-type'
import { MonitorAction } from '../../axios/action/monitor/monitor-action'
import { message } from 'antd';

export interface MonitorPanesType {
  content: JSX.Element | null,
  title: string,
  key: string,
  serverVO?: ServerVO,
  closable?: boolean,
}

class Store {
  @observable panes: MonitorPanesType[] = []
  @observable activeKey: string = '1'

  @observable loading: boolean = false

  @observable loadAvgMetric: MetricMetaVO = {} as MetricMetaVO
  @observable cpuStatMetric: MetricMetaVO = {} as MetricMetaVO
  @observable memStatMetric: MetricMetaVO = {} as MetricMetaVO

  @observable uploadStatMetric: MetricMetaVO = {} as MetricMetaVO
  @observable downloadStatMetric: MetricMetaVO = {} as MetricMetaVO

  acquireSystemStat = (form: MonitorOperateForm) => {
    this.loading = true
    MonitorAction.metric(form).then(metric => {
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
