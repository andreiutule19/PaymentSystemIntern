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

const PayHistoryList = () => {
    const [payHistory, setPayHistory] = useState([]);
    const history = useNavigate();
   


    
    useEffect(() => {
        axiosInstance.post("payments/mapHistory")
            .then(res => {
                const val = res.data;
                setPayHistory(val);
            })
            .catch(error => {
                console.log(error);
            });
    },[]);

    
    const makeOperation = (cell, row, rowIndex, formatExtraData) => {
        let x = row.status;
        let dataPay = {
            numberAccountA: row.numberAccountA,
            numberAccountB: row.numberAccountB,
            amount: row.amount,
            executor: localStorage.getItem("USER_USERNAME"),
            currency: row.currency,
            userReference: row.userReference,
            systemReference: row.systemReference,
            type: row.type
        }

        switch(x) {
            case "VERIFY":
                return (
                    <Button
                        onClick={() => {
                            console.log(row);
                    
                            history('/verify', {
                                state: {
                                    numberAccountA: row.numberAccountA,
                                    numberAccountB: row.numberAccountB,
                                    amount: row.amount,
                                    executor: row.executor,
                                    currency: row.currency,
                                    userReference: row.userReference,
                                    systemReference: row.systemReference,
                                    type: row.type
                                }
                            });
                        }}
                    >
                        Verify
                    </Button>
                );
                break;
            case "APPROVE":
                return (
                    <Button
                        variant="info"
                        onClick={() => {
                            console.log(row);
                    
                                axiosInstance.post("payments/approve", dataPay)
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
                        Approve
                    </Button>
                );
                break;
            case "AUTHORIZE":
                return (
                    <Button
                        variant="warning"
                        onClick={() => {
                            console.log(row);
                    
                                axiosInstance.post("payments/authorize", dataPay)
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
                        Authorize
                    </Button>
                );
                break;
            case "COMPLETE":
                return (
                    <Button
                        variant="success"
                        onClick={() => {
                            console.log(row);
                                axiosInstance.post("payments/complete", dataPay)
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
                        Complete
                    </Button>
                );
                break;
            
          }
    };


    const  makeCancel= (cell, row, rowIndex, formatExtraData) => {
        return (
            <Button
            variant="danger"
                onClick={() => {

                    let payHistoryData = {
                        numberAccountA:row.numberAccountA,
                        numberAccountB:row.numberAccountB,
                        amount: row.amount,
                        executor: row.executor,
                        currency:row.currency,
                        userReference: row.userReference,
                        systemReference: row.systemReference,
                        type:row.type
                    }    
                axiosInstance.post("payments/cancel", payHistoryData)
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
            Cancel Payment
          </Button>
        );
    };
    
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
            dataField: "numberAccountA",
            text: "Debitor",
            
        },

        {
                
            dataField: "numberAccountB",
            text: "Creditor",
            
        },

        {
            dataField: "amount",
            text: "Amount"

        },

        {
            dataField: "executor",
            text: "Executor"
        },

        {
            dataField: "currency",
            text: "Currency"
        },

        {
            dataField: "userReference",
            text: "User Reference"
        },

        {
            dataField: "systemReference",
            text: "System Reference"
        },

        {
            dataField: "type",
            text: "Type"
        },

        {
            dataField: "status",
            text: "Status"
        },
        {
            dataField: "action",
            text: "History Status"
        },
        
        {
            text: "Cancel",    
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return makeCancel(cell, row, rowIndex, formatExtraData);
            } 
        },
        {
            text: "Operation",
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return makeOperation(cell, row, rowIndex, formatExtraData);
            } 
        }
      

        ];
   
        return (
        <div>
          <Sidebar />
            <div className="container" style={{ padding: "20px" }}>
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={payHistory}
                    columns={columns}
                    pagination={paginationFactory({ sizePerPage: 5 })}
                    
                />
            </div>
        </div>
        );
    
 }
export default PayHistoryList;