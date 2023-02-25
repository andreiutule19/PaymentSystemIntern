import React from "react";
import { Col, Form, Row, Button } from "react-bootstrap"
import {useState} from 'react';
import axiosInstance from "./axios";
import { useLocation } from 'react-router-dom';
import history from "./history";

const EditUser = () => {
    const { state: userData } = useLocation();
    const [username, setUsername] = useState(userData.username);
    const [email, setEmail] = useState(userData.email);
    const [fullName, setFullName] = useState(userData.fullName);
    const [address, setAddress] = useState(userData.address);
    const [status] = useState(userData.status);
    const [executor] = useState(localStorage.getItem("USER_USERNAME"));
    const [error, setError] = useState({});
    const [button, setButton] = useState(false);
    const updateUser = { username, email, fullName, address, status, executor };

    const setFieldUsername = (event) => {
        const value = event.target.value
        setUsername(value)
                
    }
    const goBack = () => {
        history.push("/usersList");
        window.location.reload();
    }
        
    

    const setFieldEmail = (event) => {
        const email = event.target.value
        setEmail(email)
        const regex = /^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/
        if (email.match(regex) == null) {
            setError({
                ...error, email: "Email invalid"
            })
            setButton(true)
        }
        else {
            setError({
                ...error, email: ""
            })
            setButton(false)
        }
        
    }


    
    const setFieldFullName = (event) => {
        const value = event.target.value
        console.log(value)
        setFullName(value);
     
        if (value.length < 3) {
            setError({
                ...error, fullName: "Name is to short. Name should contain at least 3 characters"
            })
            setButton(true)
        }
        
        else {
            setError({
                ...error, fullName: ""

            })
            setButton(false)
        }
    }
    
    const setFieldAddress = (event) => {
        const value = event.target.value
        setAddress(value);
        
        if (value.length < 3) {
            setError({
                ...error, address: "Name is to short. Name should contain at least 3 characters"
            })
            setButton(true)
        }
    
        setError({
            ...error, address: ""

        })
        setButton(false)
    }
    
    

    const handleSubmit = (e) => {
        e.preventDefault();
        let credentilas = {
            username: updateUser.username,
            email: updateUser.email,
            fullName: updateUser.fullName,
            address: updateUser.address,
            status:updateUser.status,
            executor: updateUser.executor
        }
        axiosInstance.post("users/update", credentilas)
            .then(
                res => {
                    console.log("Success");
                    history.push("/approve");
                    window.location.reload();
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    return (
    <div>
        <div className="container-sm">
            <Row className="mt-5" >
                <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px', maxWidth: '500px' }}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group onChange={setFieldUsername}>
                            <Form.Label>Username</Form.Label>
                            <Form.Control type="name" placeholder="Username *" name="username" defaultValue={username} required />
                            {error.username && <p style={{ color: 'red' }}> {error.username}</p>}
                        </Form.Group>
                        <br></br>
                        <Form.Group onChange={setFieldEmail}>
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="text" placeholder="Email *" name="email" defaultValue={email} required />
                            {error.email && <p style={{ color: 'red' }}> {error.email}</p>}
                        </Form.Group>
                        <br></br>
                        <Form.Group onChange={setFieldFullName}>
                            <Form.Label>Full Name</Form.Label>
                            <Form.Control type="text" placeholder="Full Name *" name="fullName" defaultValue={fullName} required />
                            {error.fullName && <p style={{ color: 'red' }}> {error.fullName}</p>}
                        </Form.Group>
                        <br></br>
                        <Form.Group onChange={setFieldAddress}>
                            <Form.Label>Address</Form.Label>
                            <Form.Control type="text" placeholder="Address *" name="address" defaultValue={address} required />
                            {error.address && <p style={{ color: 'red' }}> {error.address}</p>}
                        </Form.Group>

                        <br></br>
                        <Button variant="success" type="submit" disabled={button} onChange={setButton} >
                            Edit 
                            </Button>
                            &nbsp;
                            &nbsp;
                            &nbsp;
                            <Button onClick={goBack}>
                                Go Back
                            </Button>   
                            
                            
                    </Form>
                </Col>
            </Row>
            
        </div>
     </div>

    )

}
export default EditUser;