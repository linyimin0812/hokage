import React from 'react'
import { Table, Result, Button } from 'antd';
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom';

const columns = [
  {
    title: "id",
    dataIndex: "id",
    key: "id"
  },
  {
    title: "主机名",
    dataIndex: "hostname",
    key: "hostname"
  },
  {
    title: "域名",
    dataIndex: "domainName",
    key: "domainName"
  },
  {
    title: "登录账号",
    dataIndex: "account",
    key: "account"
  },
  {
    title: "备注",
    dataIndex: "remark",
    key: "remark"
  },
  {
    title: "操作",
    dataIndex: "action",
    key: "action"
  },
]

const datas: any[] = []


const breadcrumProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '我的服务器'
  },
  {
    name: '我使用的服务器'
  }
]
export default class MyServer extends React.Component {

  applyServer = () => {
    window.location.href="/#/app/server/all"
  }
  render() {
    return(
      <>
      <BreadcrumbCustom breadcrumProps={breadcrumProps} />
      {
        (datas === undefined || datas.length === 0) 
        ? 
        <Result
          title="你还没有可用服务器哦,请点击申请按钮进行申请"
          extra={
            <Button type="primary" onClick={ this.applyServer }>
              申请
            </Button>
          }
        />
        :
        <Table columns={columns} dataSource={datas} />                
      }
      </>
    ) 
  }
}