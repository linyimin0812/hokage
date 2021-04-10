/* @ts-ignore */
import { ServerSearchForm } from '../../axios/action/server/server-type'
import { getHokageRole, getHokageUid } from '../../libs';
import { ServerAction } from '../../axios/action/server/server-action'
import { message } from 'antd'
import React from 'react'

export const searchServer = (component: React.Component, form: ServerSearchForm = {}) => {

    component.setState({loading: true})
    form.operatorId = getHokageUid()
    form.role = getHokageRole()
    ServerAction.searchServer(form).then(result => {
        result = (result || []).map(serverVO => {
            serverVO.key = serverVO.id + ''
            return serverVO
        })
        component.setState({dataSource: result})
    }).catch(err => message.error(err)).finally(() => component.setState({loading: false}))
}
