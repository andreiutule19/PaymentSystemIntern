import axiosInstance from "./axios";
import history from "./history";
import React from 'react';
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import './style.css'
import axiosLogin from "./axiosLogin";
import axios from "axios"
class LogIn extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
            password: "",
        };
        localStorage.clear();
    }

    
    
    handleInput = event => {
        const {value, name} = event.target;
        this.setState({
            [name]: value
        });

    };

    onSubmitFun = (e) => {
        e.preventDefault();
        let credentilas = {
            username: this.state.username,
            password: this.state.password

        }
        axiosLogin.post("users/login", credentilas)
            .then(
               res => {
                    console.log("Success");
                    localStorage.setItem("token", res.data.token);
                    localStorage.setItem("USER_USERNAME", res.data.username);
                    console.log(localStorage.getItem("token"));
                    history.push("/home");//aici sa revii ok ?
                    window.location.reload();
                }
            )
            .catch(error => {
                console.log( localStorage.getItem("USER_USERNAME"));
                alert("INVALID PASSWORD OR USERNAME");
            })
    }

    render() {
        return (
            <div className='main-container' align="center">
                <Row className="mt-5" style={{paddingTop:'150px'}} >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
                        <Form onSubmit={this.onSubmitFun.bind(this)} >
                            <Form.Group controlId="username" onChange={this.handleInput}>
                                <Form.Label>Username</Form.Label>
                                <Form.Control type="username" placeholder="Username" name="username" />
                            </Form.Group>
    
                            <Form.Group controlId="password" onChange={this.handleInput}>
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" name="password" />
                            </Form.Group>

                            <br></br>
                
            
                                <div className="d-flex justify-content-center">
                                   <p>
                                    
                                        <Button variant="primary" type="submit">
                                               Login
                                        </Button> {' '}  
                                   
                                   </p>
                                </div>
                        </Form>
                    </Col>
                </Row>
            </div>

        );
    }

}
export default LogIn;