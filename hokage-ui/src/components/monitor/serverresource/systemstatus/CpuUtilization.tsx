import React from 'react'
import { Col } from 'antd';
import ReactEcharts from 'echarts-for-react';

export default class CpuUtilization extends React.Component<any, any>{
    state = {
        UtilizationOption:  {
            title: {
                text: 'CPU利用率'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['CPU1', 'CPU2', 'CPU3'],
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
                    name: 'CPU1',
                    type: 'line',
                    stack: '总量',
                    data: [150, 232, 201, 154, 190, 330, 410]
                },
                {
                    name: 'CPU2',
                    type: 'line',
                    stack: '总量',
                    data: [320, 332, 301, 334, 390, 330, 320]
                },
                {
                    name: 'CPU3',
                    type: 'line',
                    stack: '总量',
                    data: [820, 932, 901, 934, 1290, 1330, 1320]
                }
            ]
        }
    }
    render() {
        return (
            <Col span={8} >
                <ReactEcharts option={this.state.UtilizationOption as any} style={{width: "400px"}}  />
            </Col>
        )
    }
}