// user hook
import { ServerAction } from '../axios/action/server/server-action'
import { message } from 'antd'
import { useEffect, useState } from 'react'
import { Option, ServerSearchForm, ServerVO } from '../axios/action/server/server-type'
import { getHokageRole, getHokageUid } from '../libs'

export const useServerOptions = () => {

    const [serverOptions, setServerOptions] = useState<Option[]>([])

    useEffect(() => {
        ServerAction.listServerLabelOptions().then(options => {
            setServerOptions(options || [])
        }).catch((err) => {
            message.error(err)
        })
    }, [])
    return [serverOptions]
}

export const useServerList = () => {
    const [serverList, setServerList] = useState<ServerVO[]>([])

    useEffect(() => {
        const form: ServerSearchForm = {
            operatorId: getHokageUid(),
            role: getHokageRole()
        }
        ServerAction.searchServer(form).then(value => {
            setServerList(value || [])
        }).catch(e => {
            message.error('搜索服务器失败')
        })
    }, [])
    return [serverList]
}
