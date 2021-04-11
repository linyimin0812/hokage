import React from 'react'
import { Breadcrumb } from 'antd'
import { Link } from 'react-router-dom'

export interface BreadcrumbPrpos {
	name: string,
	link?: string
}

export interface BreadcrumbProps {
	breadcrumbProps?: BreadcrumbPrpos[]
};
class BreadCrumb extends React.Component<BreadcrumbProps> {

	subBreadcrumb = () => {
		const { breadcrumbProps } = this.props
		if (breadcrumbProps === undefined) {
			return null
		}
		return breadcrumbProps.map(prop => {
			return (
				<Breadcrumb.Item>
					{prop.link === undefined ? prop.name : <Link to={prop.link}>{prop.name}</Link>}
				</Breadcrumb.Item>
			)

		})
	}

	render() {

		return (
			<span>
        		<Breadcrumb style={{ margin: '12px 0', display:'inline-block' }}>
          			{this.subBreadcrumb()}
        		</Breadcrumb>
      		</span>
		);
	}
}

export default BreadCrumb;
