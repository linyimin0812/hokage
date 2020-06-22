import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import Page from './Page';
import './style/lib/animate.css';
import './style/antd/index.less';
import './style/index.less';


ReactDOM.render(
        <Page />,
    document.getElementById('root')
);
serviceWorker.register();
