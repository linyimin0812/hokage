// user hook
import { ServerAction } from '../axios/action/server/server-action'
import { message } from 'antd'
import { useEffect, useState } from 'react'
import { Option } from '../axios/action/server/server-type'

export const useServerOptions = () => {

  const [serverOptions, setServerOptions] = useState<Option[]>([])

  useEffect(() => {
    ServerAction.listServerGroup().then(options => {
      setServerOptions(options || [])
    }).catch((err) => {
      message.error(err)
    })
  }, [])
  return [serverOptions]
}
