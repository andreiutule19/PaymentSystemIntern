import axiosInstance from "./axios";
import history from "./history";
import React from 'react';
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import {useState} from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import './style.css'
const ChangePassword = () => {
    
       
    const [passworthy,setPassworthy] = useState( {
            oldPassword: "",
            newPassword: "",
            confirmNewpassword: "",
     });
    

    const handleInput = (event) => {
        const {value, name} = event.target;
        setPassworthy({
            ...passworthy,
            [name]: value
        });

    };

    const goBack = () => {
        history.push("/approve");
        window.location.reload();
    }

    const onSubmitFun = (e) => {
        e.preventDefault();
        let credentilas = {
            oldPassword: passworthy.oldPassword,
            password: passworthy.newPassword,
            executor: localStorage.getItem("USER_USERNAME")

        }
        console.log(credentilas);
        let x = passworthy.newPassword;
        let y = passworthy.confirmNewpassword;

        if (x.localeCompare(y) === 0) {
        

            axiosInstance.post("users/changePass", credentilas)
                .then(
                    res => {
                        console.log("Success");
                        history.push("/");//aici sa revii ok ?
                        window.location.reload();
                    }
                )
                .catch(error => {
                    alert("INVALID OLD PASSWORD");
                })
        }
        else {
            alert("NO MATCH");
        }
    }

   
        return (
            <div className='main-container' align="center">
                <Row className="mt-5" style={{paddingTop:'150px'}} >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
                        <Form  >
                            <Form.Group controlId="oldPassword" onChange={handleInput}>
                                <Form.Label>Old Password</Form.Label>
                                <Form.Control type="password" placeholder="Old Password" name="oldPassword" />
                            </Form.Group>
    
                            <Form.Group controlId="newPassword" onChange={handleInput}>
                                <Form.Label>New Password</Form.Label>
                                <Form.Control type="password" placeholder="New Password" name="newPassword" />
                            </Form.Group>


                            <Form.Group controlId="confirmNewpassword" onChange={handleInput}>
                                <Form.Label> Confirm New Password</Form.Label>
                                <Form.Control type="password" placeholder="Confirm New Password" name="confirmNewpassword" />
                            </Form.Group>

                            <br></br>
                
            
                                <div className="d-flex justify-content-center">
                                   <p>
                                    
                                   <Button variant="primary" type="submit" onClick={onSubmitFun}>
                                               Change Password
                                    </Button> {' '}  
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                    <Button variant="primary" type="submit" onClick={goBack}>
                                               Go Back
                                    </Button> {' '}
                                   
                                   </p>
                                </div>
                        </Form>
                    </Col>
                </Row>
            </div>

        );
    

}
export default ChangePassword;