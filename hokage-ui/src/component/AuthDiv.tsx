import React from 'react'
import { hasPermissions } from '../libs'

export default function AuthDiv(props: any) {
  let disabled = props.disabled !== undefined
  if (props.auth && !hasPermissions(props.path)) {
    disabled = true
  }
  return disabled ? null : <div {...props}>{props.children}</div>
}
