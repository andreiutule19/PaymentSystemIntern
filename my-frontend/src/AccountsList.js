import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";
import "bootstrap/dist/css/bootstrap.min.css";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import Sidebar from "./sidebar/Sidebar";
import 'bootstrap/dist/css/bootstrap.css';
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "./axios";
import { Button } from "react-bootstrap";
import "./style.css"
import Select from 'react-select';

const AccountsList = () => {
    const [accounts, setAccounts] = useState([]);
    const history = useNavigate();
    const [currencies, setCurrencies] = useState([]);
    
    

    
    const handleChange = (e) => {
        let credentilas = {
            currency: e.value,
        }

        axiosInstance.post("accounts",credentilas)
        .then(res => {
            const val = res.data;
            setAccounts(val);
        })
        .catch(error => {
            console.log(error);
        });
    }

    useEffect(() => { 
        axiosInstance.post("accounts/currency")
        .then(res => {
            const val = res.data;
            setCurrencies(val);
        })
        .catch(error => {
            console.log(error);
        });

    },[]);
   

    let currenciesList = currencies.length > 0 &&
    currencies.map(
        (item) => {
            return (
                {label:item,value : item }
            )
        }
        );
    
    const  makeEdit = (cell, row, rowIndex, formatExtraData) => {
        return (
          <Button
                onClick={() => {
                    console.log(row);
                
                    history('/editAccounts', {
                        state: {
                            username: row.username,
                            numberAccount: row.numberAccount,
                            currency: row.currency,
                            accountStatus: row.accountStatus,
                            currentStatus:row.currentStatus
                        }
                    });
            }}
          >
            Edit
          </Button>
        );
    };


    const  accountDetails = (cell, row, rowIndex, formatExtraData) => {
        return (
            <Button
            variant="success"
                onClick={() => {
                    console.log(row);
                
                    history('/accountBalance', {
                        state: {
                            username: row.username,
                            numberAccount: row.numberAccount,
                            currency: row.currency,
                            accountStatus: row.accountStatus,
                            currentStatus:row.currentStatus
                        }
                    });
            }}
          >
            Details
          </Button>
        );
    };


    const  makeDelete = (cell, row, rowIndex, formatExtraData) => {
        return (
            <Button
            variant="danger"
                onClick={() => {

                    let accountData = {
                        username: row.username,
                        numberAccount: row.numberAccount,
                        currency: row.currency,
                        accountStatus: row.accountStatus,
                        currentStatus:row.currentStatus,
                        executor:localStorage.getItem("USER_USERNAME")
                    
                    }    
                axiosInstance.post("accounts/remove", accountData)
                .then(
                   res => {
                        window.location.reload();
                    }
                )
                .catch(error => {
        
                    alert("INVALID OP");
                })
                    
            }}
          >
            Delete
          </Button>
        );
    };
    
    const columns = [
        {
            dataField: 'id',
            text: 'ID',
            formatter: (cell, row, rowIndex, formatExtraData) => {
              return rowIndex + 1;
            },
            sort: true,
        },
       
        {
            dataField: "username",
            text: "Username"

        },

        {
                
            dataField: "numberAccount",
            text: "Number Account",
            
        },

        {
            dataField: "currency",
            text: "Currency"
        },

        {
            dataField: "accountStatus",
            text: "Account Status"
        },

        {
            dataField: "currentStatus",
            text: "Current Status"
        },


        {
            text: "Edit",    
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return makeEdit(cell, row, rowIndex, formatExtraData);
            } 
        },
        {
            text: "Delete",
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return makeDelete(cell, row, rowIndex, formatExtraData);
            } 
        },
        {
            text: "Details",
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return accountDetails(cell, row, rowIndex, formatExtraData);
            } 
        }
      

        ];
   
        return (
        <div>
          <Sidebar />
                <div className="container" style={{ padding: "20px" }}>
                <div className="container" style={{maxWidth:"220px" }}>
                        <Select options={currenciesList} onChange={handleChange} />
                </div>
                    <div className="container" style={{ padding: "20px" }}>
                  
                          <BootstrapTable
                               striped hover condensed
                               keyField="id"
                               data={accounts}
                               columns={columns}
                               pagination={paginationFactory({ sizePerPage: 5 })}
                    
                    />
                    </div>
            </div>
        </div>
        );
    
 }
export default AccountsList;