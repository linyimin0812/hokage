import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import Page from './Page';
// import { AppContainer } from 'react-hot-loader';
import './style/lib/animate.css';
import './style/antd/index.less';
import './style/index.less';


ReactDOM.render(
    // <AppContainer>
        <Page />,
    // </AppContainer>
    document.getElementById('root')
);
serviceWorker.register();
