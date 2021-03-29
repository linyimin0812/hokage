import React from 'react'
import { Button, Result } from 'antd'

export default class Index extends React.Component<any, any> {

    backHome = () => {
        window.location.href="/#/"
    }

    render() {
        return (
            <Result
                status="404"
                title="此功能暂未实现, 敬请期待"
                extra={<Button onClick={this.backHome} type="primary" >返回首页</Button>}
            />
        );
    }
}