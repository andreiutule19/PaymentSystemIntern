import axiosInstance from "./axios";
import history from "./history";
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import Sidebar from "./sidebar/Sidebar";
import React, { useEffect, useState } from "react";
import Select from 'react-select';
const MakePayment = () => {
    
    const [newPayment, setNewPayment] =
        useState({
            numberAccountA: "",
            numberAccountB: "",
            amount: 0,
            userReference: ""
        });

    const [currencies, setCurrencies] = useState([]);
    const [newCurrency, setNewCurrency] = useState();
    const [newType, setType] = useState({
        myType: ""
    }
    );


    const  [newExternalPayment,setNewExternalPayment]  = 
        useState ({
            senderIBAN:"",
            receiverIBAN: "",
            amount:0,
            senderName:""
        });
    const [newBic, setNewBic] = useState();
    const [bics, setBics] = useState([]);
    useEffect(() => {

            axiosInstance.post("payments/bic")
                .then(res => {
                    const val = res.data;
                    setBics(val);
                })
                .catch(error => {
                    console.log(error);
                });


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
        setNewCurrency(e.value);
    }

    const handleBIC = (e) => {
        setNewBic(e.value);
    }

    const handleType = (e) => {
        setType({
            myType:e.value
        }
        );
    }

    let currenciesList = currencies.length > 0 &&
        currencies.map(
            (item) => {
                return (
                    {label:item,value : item }
                )
            }
        );
    
    let bicsList = bics.length > 0 &&
        bics.map(
            (item) => {
                return (
                    {label:item,value : item }
                )
            }
        );
    
    const typeList = [
    {
        label: "Internal",
        value: "Internal",
    },
    {
        label: "External",
        value: "External",
     }   
    ];
    
    
    const handleInput = (event) => {
        const {value, name} = event.target;
        setNewPayment({
            ...newPayment,
            [name]: value
        });

    };

    const handleInput2 = (event) => {
        const {value, name} = event.target;
        setNewExternalPayment({
            ...newExternalPayment,
            [name]: value
        });

    };
   
    const onSubmitFun = (e) => {
        e.preventDefault();
        let x = newType.myType;
        if (x.localeCompare("Internal") === 0) {
            let credentilas = {
                numberAccountA: newPayment.numberAccountA,
                numberAccountB: newPayment.numberAccountB,
                amount: newPayment.amount,
                executor: localStorage.getItem("USER_USERNAME"),
                currency: newCurrency,
                userReference: newPayment.userReference,
                type: newType.myType
        
            }
            console.log(credentilas);
        
            axiosInstance.post("payments/insert", credentilas)
                .then(
                    res => {
                        console.log(credentilas);
                        console.log("Success");
                        history.push("/payList");
                        window.location.reload();
                    }
                )
                .catch(error => {
                    alert("INVALID ");
                })
        }
        else {
        
            let credits = {
                senderIBAN: newExternalPayment.senderIBAN,
                receiverIBAN: newExternalPayment.receiverIBAN,
                amount: newExternalPayment.amount,
                senderName: newExternalPayment.senderName,
                BICReceiver: newBic,
                type: newType.myType,
                currency: newCurrency,
                executor: localStorage.getItem("USER_USERNAME"),
                userReference: newExternalPayment.userReference,
                    
            }
            console.log(credits);
        
            
            axiosInstance.post("payments/external", credits)
                .then(
                    res => {
                        console.log(credits);
                        console.log("Success");
                        history.push("/payList");
                        window.location.reload();
                    }
                )
                .catch(error => {
                    alert("INVALID ");
                })
        }

       
      
    }

    const makeOperation = () => {
       
        switch (newType.myType) {
            case "External":
                return (
                    <div>
                        <Form.Group controlId="senderIBAN" onChange={handleInput2}>
                           <Form.Label>SENDER IBAN</Form.Label>
                           <Form.Control  placeholder="IBAN DEBITOR" name="senderIBAN" />
                        </Form.Group>
            
                        <Form.Group controlId="receiverIBAN" onChange={handleInput2}>
                           <Form.Label>RECEIVER IBAN</Form.Label>
                           <Form.Control  placeholder="IBAN CREDITOR" name="receiverIBAN" />
                        </Form.Group>
                     
                        <Form.Group controlId="amount" onChange={handleInput2}>
                           <Form.Label>Amount</Form.Label>
                           <Form.Control type="number" placeholder="Amount" name="amount" />
                        </Form.Group>
                     
                        <Form.Group controlId="userReference" onChange={handleInput2}>
                           <Form.Label>User Reference</Form.Label>
                           <Form.Control  placeholder="User Refrence" name="userReference" />
                       </Form.Group>
                     
                     
                       <Form.Group controlId="senderName" onChange={handleInput2}>
                           <Form.Label>Sender Name</Form.Label>
                           <Form.Control  placeholder="Sender Name" name="senderName" />
                       </Form.Group>
                      
                        <label style={{padding: '10px'}}>Option BIC</label>
                        <Select options={bicsList} onChange={handleBIC} />

                        <label style={{padding: '10px'}}>Option currency</label>
                        <Select options={currenciesList} onChange={handleChange}/>
                    </div>
                );
            case "Internal":
                return (
                    <div>
                        <Form.Group controlId="numberAccountA" onChange={handleInput}>
                           <Form.Label>IBAN DEBITOR</Form.Label>
                           <Form.Control  placeholder="IBAN DEBITOR" name="numberAccountA" />
                        </Form.Group>
            
                        <Form.Group controlId="numberAccountB" onChange={handleInput}>
                           <Form.Label>IBAN CREDITOR</Form.Label>
                           <Form.Control  placeholder="IBAN CREDITOR" name="numberAccountB" />
                        </Form.Group>
                     
                        <Form.Group controlId="amount" onChange={handleInput}>
                           <Form.Label>Amount</Form.Label>
                           <Form.Control type="number" placeholder="Amount" name="amount" />
                        </Form.Group>
                     
                        <Form.Group controlId="userReference" onChange={handleInput}>
                           <Form.Label>User Reference</Form.Label>
                           <Form.Control  placeholder="User Refrence" name="userReference" />
                       </Form.Group>
                     
                        <label style={{padding: '10px'}}>Option currency</label>
                        <Select options={currenciesList} onChange={handleChange}/>
                    </div>
                );
        }

        
          
     };
 

    return (
         
        
        <div>
            <Sidebar />
            
               <div className='main-container' align="center">
                  <Row className="mt-5" >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
            
                        <Form >
                            {makeOperation()} 
                            <label style={{padding: '10px'}}>Option payment</label>
                             <Select options={typeList} onChange={handleType} defaultValue={"Internal"} />


                             <br></br>
                             <div className="d-flex justify-content-center">
                                <p> 
                                    <Button variant="primary" type="submit" onClick={onSubmitFun}>
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
export default MakePayment;