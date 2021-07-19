import React from 'react'
import {
  Row,
  Col,
  Card,
  Comment,
  Tooltip,
  List,
  PageHeader,
  Descriptions,
  Divider,
  Avatar, Spin,
} from 'antd';
import moment from 'moment'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import store from './store'
import { observer } from 'mobx-react'
import AverageLoad from '../monitor/system-status/average-load';
import CpuUtilization from '../monitor/system-status/cpu-utilization';
import RamUsage from '../monitor/system-status/ram-usage'
import UploadSpeed from '../monitor/network/upload-speed';
import DownloadSpeed from '../monitor/network/download-speed';

const data = [
  {
    actions: [<span key="comment-list-reply-to-0">Reply to</span>],
    author: 'System',
    avatar: <Avatar style={{ backgroundColor: '#F56A00', verticalAlign: 'middle' }} size="large">{'Hokage'}</Avatar>,
    content: <p>欢迎使用Hokage服务器管理系统</p>,
    datetime: (
      <Tooltip title={moment().format('YYYY-MM-DD HH:mm:ss')}>
        <span>now</span>
      </Tooltip>
    ),
  },
]

const breadcrumbProps: BreadcrumbProps[] = [{ name: '首页', link: '/app/index' }]

@observer
class Index extends React.Component {

  componentDidMount() {
    store.fetchHomeDetail()
    store.fetchHomeSystemMetric()
  }

  renderDescriptions = (key: string, value: string | number) => {
    return <Descriptions.Item label={key}>{value}</Descriptions.Item>
  }

  render() {
    const {allVO, availableVO, accountVO} = store.homeDetailVO
    const { loadAvgMetric, cpuStatMetric, memStatMetric, uploadStatMetric, downloadStatMetric } = store.systemMetric
    return (
      <div className="gutter-example button-demo">
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
          <Divider orientation="left">所有服务器信息概览</Divider>
          <Spin spinning={store.isFetching}>
            <Row gutter={16} >
              <Col span={8}>
                <Card>
                  <PageHeader className="site-page-header" title="主机总数" subTitle={allVO.total}>
                    <Descriptions size="default" column={2}>
                      { Object.keys(allVO.groupInfo).map(key => this.renderDescriptions(key, allVO.groupInfo[key])) }
                    </Descriptions>
                  </PageHeader>
                </Card>
              </Col>

              <Col span={8}>
                <Card>
                  <PageHeader className="site-page-header" title="可用主机总数" subTitle={availableVO.total}>
                    <Descriptions size="default" column={2}>
                      { Object.keys(allVO.groupInfo).map(key => this.renderDescriptions(key, availableVO.groupInfo[key])) }
                    </Descriptions>
                  </PageHeader>
                </Card>
              </Col>

              <Col span={8}>
                <Card>
                  <PageHeader className="site-page-header" title="全部用户数" subTitle={accountVO.total}>
                    <Descriptions size="default" column={2}>
                      { Object.keys(allVO.groupInfo).map(key => this.renderDescriptions(key, accountVO.groupInfo[key])) }
                    </Descriptions>
                  </PageHeader>
                </Card>
              </Col>
            </Row>
          </Spin>
        </div>
        <Divider style={{margin: '4px 0px'}} />
        <div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
          <Divider orientation="left">{`CPU使用率最高-${store.serverIp}`}</Divider>
          <Spin spinning={store.isMetricFetching}>
            <Row gutter={12} align="middle" justify={"center"} >
              <Col span={8}><AverageLoad data={loadAvgMetric} id={store.serverId} /></Col>
              <Col span={8}><CpuUtilization data={cpuStatMetric} id={store.serverId} /></Col>
              <Col span={8}><RamUsage data={memStatMetric} id={store.serverId} /></Col>
              <Col span={12}><UploadSpeed data={uploadStatMetric} id={store.serverId} /></Col>
              <Col span={12}><DownloadSpeed data={downloadStatMetric} id={store.serverId} /></Col>
            </Row>
          </Spin>
        </div>
        <Divider style={{margin: '4px 0px'}} />
        <div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
          <Divider orientation="left">我的消息</Divider>
          <Card bordered={false}>
            <List
              className="comment-list"
              itemLayout="horizontal"
              dataSource={data}
              renderItem={item => (
                <li>
                  <Comment actions={item.actions} author={item.author} avatar={item.avatar} content={item.content} datetime={item.datetime} />
                </li>
              )}
            />
          </Card>
        </div>
      </div>
    )
  }
}

export default Index;
