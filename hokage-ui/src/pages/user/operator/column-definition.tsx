import { Button, Tag } from 'antd'
import { randomColor } from '../../../libs'
import React from 'react'
import { Operation, UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { BreadcrumbPrpos } from '../../../layout/bread-crumb'
import { Action } from '../../../component/Action'
import { SelectServer } from '../../server/select-server'
import { FormInstance } from 'antd/lib/form'

/**
 * @author linyimin
 * @date 2021/2/19 12:34 am
 * @email linyimin520812@gmail.com
 * @description
 */


export const nestedColumn = [
  {
    title: 'id',
    dataIndex: 'id',
    key: 'id'
  },
  {
    title: '主机名',
    dataIndex: 'hostname',
    key: 'hostname'
  },
  {
    title: '域名',
    dataIndex: 'domain',
    key: 'domain'
  },
  {
    title: '标签',
    dataIndex: 'labels',
    key: 'labels',
    render: (labels: string[]) => {
      return labels.map((tag: any )=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>)
    }
  },
  {
    title: '使用人数',
    dataIndex: 'userNum',
    key: 'userNum'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    render: (text: string) => <Tag color = {randomColor(text)}> { text } </Tag>
  },
  {
    title: '操作',
    dataIndex: 'operationList',
    key: 'operationList',
    render: (operationList: Operation[]) => operationList.map(operation => <Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
  }
]

export const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    key: 'id'
  },
  {
    title: '姓名',
    dataIndex: 'username',
    key: 'username'
  },
  {
    title: '负责服务器数量',
    dataIndex: 'serverNum',
    key: 'serverNum'
  },
  {
    title: '服务器标签',
    dataIndex: 'serverLabelList',
    key: 'serverLabelList',
    render: (serverLabelList: string[]) => serverLabelList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  },
  {
    title: '操作',
    key: 'action',
    render: (_: any, record: UserVO) => {
      return (
        <Action>
          <Action.Form
            title={'添加服务器'}
            renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
            action={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
          />
          <Action.Form
            title={'回收服务器'}
            renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
            action={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
          />
          <Action.Confirm
            title={'删除'}
            action={async () => {alert('TODO: 添加删除动作')}}
            content={`确定删除管理员${record.username}(${record.id})`}
          />
        </Action>
      )
    },
  }
]

export const breadcrumbProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '用户管理'
  },
  {
    name: '服务器管理员'
  }
]
