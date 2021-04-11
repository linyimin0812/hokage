import React from 'react'
import { Link as ALink } from 'react-router-dom'
import { Button as AButton, Divider } from 'antd'

class Action extends React.Component {
    static Link(props: any) {
        return <ALink {...props} />
    }

    static Button(props: any) {
        return <AButton type={'link'} {...props} style={{padding: 0}} />
    }

    _handle = (children: React.ReactNode[], el: React.ReactNode) => {
        const length = children.length
        if (length !== 0) {
            children.push(<Divider key={length} type={'vertical'} />)
        }
        children.push(el)
    }

    render() {
        const children: React.ReactNode[] = []
        if (Array.isArray(this.props.children)) {
            this.props.children.forEach(el => this._handle(children, el))
        } else {
            this._handle(children, this.props.children as React.ReactElement)
        }
        return (
            { children }
        )
    }

}
