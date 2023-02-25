import { Col, Form, Row, Button } from "react-bootstrap"
import React, { useEffect, useState } from "react";
import axiosInstance from "./axios";
import { useLocation } from 'react-router-dom';
import history from "./history";
import Select from 'react-select';

const EditAccount = () => {
    const { state: accountData } = useLocation();
    const [username, setUsername] = useState(accountData.username);
    const [numberAccount] = useState(accountData.numberAccount);
    const [currency, setCurrency] = useState(accountData.currency);
    const [executor] = useState(localStorage.getItem("USER_USERNAME"));
    const [accountStatus, setAccountStatus] = useState(accountData.accountStatus);
    const [currentStatus] = useState(accountData.currentStatus);
    const [button, setButton] = useState(false);
    const updateAccount = { username, numberAccount, currency, executor, accountStatus, currentStatus };
    const [currencies, setCurrencies] = useState([]);
    

    useEffect(() => {
        axiosInstance.post("accounts/currency")
            .then(res => {
                const val = res.data;
                setCurrencies(val);
            })
            .catch(error => {
                console.log(error);
            });
    }, []);

    const handleChange = (e) => {
        setCurrency(e.value);
    }

    const handleChange2 = (e) => {
        setAccountStatus(e.value);
    }

    const goBack = () => {
        history.push("/approveAccount");
        window.location.reload();
    }

    let currenciesList = currencies.length > 0 &&
    currencies.map(
        (item) => {
            return (
                {label:item,value : item }
            )
        }
        );
    
    const myOptions = [
        { value: 'OPEN', label: 'OPEN' },
        { value: 'BLOCKED', label: 'BLOCKED' },
        { value: 'BLOCK_DEBIT', label: 'BLOCK_DEBIT' },
        { value: 'BLOCK_CREDIT', label: 'BLOCK_CREDIT' },
        { value: 'CLOSED', label: 'CLOSED' },
    ]


    const setFieldUsername = (event) => {
        const value = event.target.value
        setUsername(value)
                
    } 

    const handleSubmit = (e) => {
        e.preventDefault();
        let credentilas = {
            username: updateAccount.username,
            numberAccount: updateAccount.numberAccount,
            currency: updateAccount.currency,
            executor: updateAccount.executor,
            accountStatus: updateAccount.accountStatus,
            currentStatus:updateAccount.currentStatus
        }
        axiosInstance.post("accounts/update", credentilas)
            .then(
                res => {
                    console.log("Success");
                    history.push("/approveAccount");
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
                        </Form.Group>
                            <br></br>
                            
                        <Select options={currenciesList} onChange={handleChange} />
                        <br></br>
                        <Select options={myOptions} onChange={handleChange2} />
                        

                        <br></br>
                        <Button onClick={handleSubmit} variant="success" type="submit" disabled={button} onChange={setButton} >
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
export default EditAccount ;