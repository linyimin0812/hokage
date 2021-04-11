import React from 'react'
import img from '../style/imgs/404.png'

class NotFound extends React.Component {
    state = {
        animated: '',
    };
    render() {
        return (
            <div
                style={{ height: '100%', background: '#ececec', overflow: 'hidden', textAlign: 'center' }}
            >
                <img
                    src={img}
                    alt="404"
                    style={{width: '100%', height: '100%'}}
                />
            </div>
        )
    }
}

export default NotFound
