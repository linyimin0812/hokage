import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import Page from './Page';
import * as apis from './axios';
// import { AppContainer } from 'react-hot-loader';
import { AlitaProvider, setConfig } from 'redux-alita';
import './style/lib/animate.css';
import './style/antd/index.less';
import './style/index.less';

setConfig(apis);

ReactDOM.render(
    // <AppContainer>
    <AlitaProvider>
        <Page />
    </AlitaProvider>,
    // </AppContainer>
    document.getElementById('root')
);
serviceWorker.register();
