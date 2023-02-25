import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import Sidebar from './sidebar/Sidebar';
class Home extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
            password: "",
        };

    }
    render() {
        return (
            <div className='main-container' align="center">
                <Sidebar />
                <p>He he .. we re home </p>
            </div>

        );
    }

}
export default Home;