import axiosInstance from "./axios";
import history from "./history";
import Button from 'react-bootstrap/Button';
import { Col, Form, Row } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import Sidebar from "./sidebar/Sidebar";
import React, { useEffect, useState } from "react";
import Select from 'react-select';
const CreateAccount = () =>{
    
    const  [newAccount,setNewAccount]  = 
        useState ({
            username:"",
            numberAccount: "",
            executor:""

        });

    const [currencies, setCurrencies] = useState([]);
    const [newCurrency, setNewCurrency] = useState();

    

    useEffect(() => {
        axiosInstance.post("accounts/currency", {
            headers: {
                Authorization: 'Bearer '+localStorage.getItem("user-token")
            }
        }
        )
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
        let currenciesList = currencies.length > 0 &&
        currencies.map(
            (item) => {
                return (
                    {label:item,value : item }
                )
            }
        );
    
    
    const handleInput = (event) => {
        const {value, name} = event.target;
        setNewAccount({
            ...newAccount,
            [name]: value
        });

    };

    const onSubmitFun = (e) => {
        e.preventDefault();
        let credentilas = {
            username:newAccount.username,
            numberAccount:newAccount.numberAccount,
            currency:newCurrency,
            executor:localStorage.getItem("USER_USERNAME")

        }
        console.log(credentilas);

        axiosInstance.post("accounts/insert", credentilas)
                .then(
                    res => {
                        console.log("Success");
                        history.push("/accountsList");
                        window.location.reload();
                    }
                )
                .catch(error => {
                    alert("INVALID ");
                })
      
    }

     return (
            <div>
                <Sidebar/>
               <div className='main-container' align="center">
                  <Row className="mt-5" >
                    <Col lg={5} md={6} sm={12} className="p-5 m-auto shadow-sm rounded-sm" style={{ backgroundColor: 'white', borderRadius: '20px' ,maxWidth:'500px'}}>
                         <Form >
                             
                             <Form.Group controlId="username" onChange={handleInput}>
                                <Form.Label>Username</Form.Label>
                                <Form.Control  placeholder="Username" name="username" />
                             </Form.Group>

                            <Form.Group controlId="numberAccount" onChange={handleInput}>
                                <Form.Label>Account Number</Form.Label>
                                <Form.Control  placeholder="Number Account" name="numberAccount" />
                             </Form.Group>
                             
                             
                            <br></br>
                 
                            <Select options={currenciesList} onChange={handleChange} defaultValue={currenciesList[0]} />

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
export default CreateAccount;