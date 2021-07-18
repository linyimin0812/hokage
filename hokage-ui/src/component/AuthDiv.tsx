import React from 'react'
// import { hasPermissions } from '../libs'

type AuthDivPropType = {
  auth: boolean,
  children: any,
}

export default function AuthDiv(props: AuthDivPropType) {
  return props.auth ? <div {...props}>{props.children}</div> : null
}
