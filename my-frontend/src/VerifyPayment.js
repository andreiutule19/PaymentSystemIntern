import axiosInstance from "./axios";
import history from "./history";
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import Sidebar from "./sidebar/Sidebar";
import React, { useEffect, useState } from "react";
import Select from 'react-select';
import { useLocation } from 'react-router-dom';
const VerifyPayment = () => {
    const { state: paymentData } = useLocation();
    const [numberAccountA] = useState(paymentData.numberAccountA);
    const [numberAccountB] = useState(paymentData.numberAccountB);
    const [amount,setNewAmount] = useState(paymentData.amount);
    const [executor] = useState(localStorage.getItem("USER_USERNAME"));
    const [currency] = useState(paymentData.currency);
    const [userReference] = useState(paymentData.userReference);
    const [systemReference] = useState(paymentData.systemReference);
    const [type] = useState(paymentData.type);
    
    const newPayment = {
        numberAccountA, numberAccountB, amount, executor, currency,
        userReference, systemReference, type
    };

    const goBack = () => {
        history.push("/payList");
        window.location.reload();
    }

    const handleInput = (event) => {
        const {value, name} = event.target;
        setNewAmount(
            value
        );

    };

    const onSubmitFun = (e) => {
        e.preventDefault();
        let credentilas = {
            numberAccountA:newPayment.numberAccountA,
            numberAccountB:newPayment.numberAccountB,
            amount: newPayment.amount,
            executor: newPayment.executor,
            currency:newPayment.currency,
            userReference: newPayment.userReference,
            systemReference: newPayment.systemReference,
            type:newPayment.type
        }

        axiosInstance.post("payments/verify", credentilas)
                .then(
                    res => {
                        console.log(res.status);
                        console.log("Success");
                        history.push("/payList");
                        window.location.reload();
                    }
                )
            .catch(error => {
               
                    console.log(error);
                    alert("INVALID ");
                })
      
    }

     return (
            <div>
               <div className='main-container' align="center">
                  <Row className="mt-5" >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
                         <Form >
                             
                             <Form.Group controlId="numberAccountA" >
                                <Form.Label>IBAN DEBITOR</Form.Label>
                                <Form.Control  disabled defaultValue={numberAccountA} placeholder="IBAN DEBITOR" name="numberAccountA" />
                             </Form.Group>

                             <Form.Group controlId="numberAccountB">
                                <Form.Label>IBAN CREDITOR</Form.Label>
                                <Form.Control disabled   defaultValue={numberAccountB} placeholder="IBAN CREDITOR" name="numberAccountB" />
                             </Form.Group>
                             
                             <Form.Group controlId="amount" >
                                <Form.Label>Amount</Form.Label>
                                <Form.Control disabled defaultValue={amount} type="number" placeholder="Amount" name="amount" />
                             </Form.Group>
                             
                             <Form.Group controlId="userReference" >
                                <Form.Label>User Reference</Form.Label>
                                <Form.Control disabled defaultValue={userReference} placeholder="User Refrence" name="userReference" />
                             </Form.Group>
                             
                             <Form.Group controlId="systemReference" >
                                <Form.Label>System Refrence</Form.Label>
                                <Form.Control disabled defaultValue={systemReference} placeholder="System Refrence" name="systemReference" />
                             </Form.Group>


                             <Form.Group controlId="amount" onChange={handleInput}>
                                <Form.Label>Verify amount</Form.Label>
                                <Form.Control  placeholder="Amount" name="amount" />
                             </Form.Group>
                             <br></br>
                             
                             <div className="d-flex justify-content-center">
                                <p> 
                                    <Button variant="primary" type="submit" onClick={onSubmitFun}>
                                        Verify
                                    </Button> {' '}  
                                     &nbsp;
                                     &nbsp;
                                    &nbsp;
                            <Button onClick={goBack}>
                                Go Back
                            </Button>   
                            
                                </p>
                             </div>
                        </Form>
                    </Col>
                </Row>
                </div>
             </div>

    );
    

}
export default VerifyPayment;