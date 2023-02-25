import axiosInstance from "./axios";
import history from "./history";
import BootstrapTable from "react-bootstrap-table-next";
import 'bootstrap/dist/css/bootstrap.css';
import React, { useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import { Col, Form, Row } from "react-bootstrap";
import { Button } from "react-bootstrap";
const AccountBalance = () => {
    const { state: accountData } = useLocation();
    const [username] = useState(accountData.username);
    const [numberAccount] = useState(accountData.numberAccount);
    const [currency] = useState(accountData.currency);
    const [accountStatus] = useState(accountData.accountStatus);
    const [currentStatus] = useState(accountData.currentStatus);
    const [balances, setBalances] = useState([]);

    const goBack = () => {
        history.push("/accountsList");
        window.location.reload();
    }

    let myData ={ numberAccount:numberAccount}
   
    useEffect(() => {
        axiosInstance.post("accounts/balance",myData)
            .then(res => {
                const val = [];
                val.push(res.data);
                setBalances(val);
            })
            .catch(error => {
                console.log(error);
            });
    },[]);
    

    const columns = [
        {
            dataField: 'index',
            text: 'ID',
            formatter: (cell, row, rowIndex, formatExtraData) => {
              return rowIndex + 1;
            },
            sort: true,
        },
       
        {
            dataField: "numberAccount",
            text: "IBAN",
            
        },
        {
            dataField: "availableBalance",
            text: "Available Balance",
            
        },

        {
                
            dataField: "availableCreditAmount",
            text: "ACA",
            
        },

        {
            dataField: "availableCreditCount",
            text: "ACC"

        },

        {
            dataField: "availableDebitAmount",
            text: "ADA"
        },

        {
            dataField: "availableDebitCount",
            text: "ADC"
        },

        {
            dataField: "pendingCreditAmount",
            text: "PCA"
        },

        {
            dataField: "pendingDebitAmount",
            text: "PDA"
        },

        {
            dataField: "pendingCreditCount",
            text: "PCC"
        },

        {
            dataField: "pendingDebitCount",
            text: "PDC"
        }

    ];
    
     return (
         <div>
             
<div className='main-container' align="center">
                  <Row className="mt-5" >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
                         <Form >
                             
                             <Form.Group controlId="username" >
                                <Form.Label>Username</Form.Label>
                                <Form.Control  disabled defaultValue={username}  name="username" />
                             </Form.Group>

                             <Form.Group controlId="numberAccount">
                                <Form.Label>IBAN</Form.Label>
                                <Form.Control disabled   defaultValue={numberAccount}  name="numberAccount" />
                             </Form.Group>
                             
                             <Form.Group controlId="currency" >
                                <Form.Label>Amount</Form.Label>
                                <Form.Control disabled defaultValue={currency}  name="amount" />
                             </Form.Group>
                             
                             <Form.Group controlId="accountStatus" >
                                <Form.Label>Account Status</Form.Label>
                                <Form.Control disabled defaultValue={accountStatus} name="accountStatus" />
                             </Form.Group>
                             
                             <Form.Group controlId="currentStatus" >
                                <Form.Label>Current Status</Form.Label>
                                <Form.Control disabled defaultValue={currentStatus}  name="currentStatus" />
                             </Form.Group>
                             <br></br>
                             
                             <div className="d-flex justify-content-center">
                                <p> 
                            <Button onClick={goBack}>
                                Go Back
                            </Button>   
                            
                                </p>
                             </div>
                        </Form>
                    </Col>
                </Row>
             </div>


             <div className="container" style={{ padding: "40px" }}>
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={balances}
                    columns={columns}
                    
                />
            </div>
    </div>

    );
    

}
export default AccountBalance;