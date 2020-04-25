/**
 * Created by hao.cheng on 2017/5/3.
 */
import React from 'react';
import { 
  Row,
  Col,
  Card,
  Icon,
  Comment,
  Tooltip,
  List
} from 'antd';
import moment from 'moment'
import BreadcrumbCustom from '../BreadcrumbCustom';

const data = [
  {
    actions: [<span key="comment-list-reply-to-0">Reply to</span>],
    author: 'Han Solo',
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    content: (
      <p>
        We supply a series of design principles, practical patterns and high quality design
        resources (Sketch and Axure), to help people create their product prototypes beautifully and
        efficiently.
      </p>
    ),
    datetime: (
      <Tooltip
        title={moment()
          .subtract(1, 'days')
          .format('YYYY-MM-DD HH:mm:ss')}
      >
        <span>
          {moment()
            .subtract(1, 'days')
            .fromNow()}
        </span>
      </Tooltip>
    ),
  },
  {
    actions: [<span key="comment-list-reply-to-0">Reply to</span>],
    author: 'Han Solo',
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    content: (
      <p>
        We supply a series of design principles, practical patterns and high quality design
        resources (Sketch and Axure), to help people create their product prototypes beautifully and
        efficiently.
      </p>
    ),
    datetime: (
      <Tooltip
        title={moment()
          .subtract(2, 'days')
          .format('YYYY-MM-DD HH:mm:ss')}
      >
        <span>
          {moment()
            .subtract(2, 'days')
            .fromNow()}
        </span>
      </Tooltip>
    ),
  },
];

class Dashboard extends React.Component {
    render() {
        return (
            <div className="gutter-example button-demo">
                <BreadcrumbCustom />
                <Row gutter={16} title="test">
                  <Card title="服务器信息概览" bordered>
                    <Col span={6}>
                      <Card type="inner" title="主机总数" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title="可用主机总数" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title="全部用户数" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title="在线用户数" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                  </Card>
                </Row>
                
                <Row gutter={16}>
                  <Card title="我的服务器信息概览" bordered>
                    <Col span={6}>
                      <Card type="inner" title="主机总数" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title={<div>可用主机总数</div>} >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title="已连接服务器" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                    
                    <Col span={6}>
                      <Card type="inner" title="最近登录时间" >
                        <span>普通服务器</span>
                        <span>GPU服务器</span>
                      </Card>
                    </Col>
                  </Card>
                </Row>
                
                <Row>
                        <div className="gutter-box">
                            <Card bordered={false}>
                                <div className="pb-m">
                                    <h3>消息栏</h3>
                                </div>
                                <span className="card-tool"><Icon type="sync" /></span>
                                <List
                                  className="comment-list"
                                  header={`${data.length} replies`}
                                  itemLayout="horizontal"
                                  dataSource={data}
                                  renderItem={item => (
                                    <li>
                                      <Comment
                                        actions={item.actions}
                                        author={item.author}
                                        avatar={item.avatar}
                                        content={item.content}
                                        datetime={item.datetime}
                                      />
                                    </li>
                                  )}
                                />
                            </Card>
                        </div>
                </Row>
            </div>
        )
    }
}

export default Dashboard;