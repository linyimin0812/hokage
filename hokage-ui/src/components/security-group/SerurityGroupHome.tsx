import React from 'react'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../bread-crumb-custom'
import EnterSecurity from './EnterSecurity'
import Header from './Header'

const breadcrumProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '安全组'
    }
]

interface SecurityGroupHomeSateType {
    isAddNew: boolean
}

export default class SecurityGroupHome extends React.Component<{}, SecurityGroupHomeSateType> {

    state = {
        isAddNew: false
    }

    onClick = () => {
        this.setState({ isAddNew: true })
    }

    onSearch = (address: string) => {
        this.setState({ isAddNew: true })
    }

    cancelAddNew = () => {
        this.setState({ isAddNew: false })
    }

    render() {
        return (
            <div>
                <BreadcrumbCustom breadcrumProps={breadcrumProps} />
                <Header onClick={this.onClick} onSearch={this.onSearch} />
                <EnterSecurity isAddNew={this.state.isAddNew} cancelAddNew={this.cancelAddNew} />
            </div>
        )
    }
}