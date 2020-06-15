import React from 'react'
import { Col } from 'antd';
import ReactEcharts from 'echarts-for-react';

export default class RamUsage extends React.Component<any, any>{
    state = {
        RamUsageOption:  {
            title: {
                text: '内存使用量'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['memory'],
                orient: "horizontal",
                x: "center",
                y: "bottom"
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '10%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['20:00', '20:10', '20:20', '20:30', '20:40', '20:50', '21:00']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: 'memory',
                    type: 'line',
                    stack: '总量',
                    data: [150, 232, 201, 154, 190, 330, 410]
                }
            ]
        }
    }
    render() {
        return (
            <Col span={8} >
                <ReactEcharts option={this.state.RamUsageOption as any} style={{width: "400px"}} />
            </Col>
        )
    }
}