import axiosInstance from "./axios";
import history from "./history";
import React from 'react';
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import Sidebar from "./sidebar/Sidebar";
class Register extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
            password: "",
            confirmPass:"",
            email: "",
            address: "",
            fullName: ""

        };
    
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
            email: this.state.email,
            password: this.state.password,
            fullName:this.state.fullName,
            address: this.state.address,
            executor:localStorage.getItem("USER_USERNAME")

        }

        if (this.state.password.localeCompare(this.state.confirmPass) === 0) {
           
        
            axiosInstance.post("users/insert", credentilas)
                .then(
                    res => {
                        console.log("Success");
                        history.push("/home");
                        window.location.reload();
                    }
                )
                .catch(error => {
                    alert("INVALID ");
                })
        }
        else {
    
            alert("Password dont match");
        }
    }


    render() {
        return (
            <div>
                <Sidebar/>
               <div className='main-container' align="center">
                  <Row className="mt-5" >
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


                            <Form.Group controlId="confirmPass" onChange={this.handleInput}>
                                <Form.Label> Confirm Password</Form.Label>
                                <Form.Control type="password" placeholder="Confirm Password" name="confirmPass" />
                            </Form.Group>

                            <Form.Group controlId="email" onChange={this.handleInput}>
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="username" placeholder="Email" name="email" />
                            </Form.Group>


                            <Form.Group controlId="address" onChange={this.handleInput}>
                                <Form.Label>Address</Form.Label>
                                <Form.Control type="username" placeholder="Address" name="address" />
                            </Form.Group>


                            <Form.Group controlId="fullName" onChange={this.handleInput}>
                                <Form.Label>Full Name</Form.Label>
                                <Form.Control type="username" placeholder="Full Name" name="fullName" />
                            </Form.Group>

                            <br></br>
            
                                <div className="d-flex justify-content-center">
                                <p>
                                    
                                <Button variant="primary" type="submit">
                                    Create
                                </Button> {' '}  
                                   
                            
                                    </p>
                                </div>
                        </Form>
                    </Col>
                </Row>
                </div>
             </div>

        );
    }

}
export default Register;