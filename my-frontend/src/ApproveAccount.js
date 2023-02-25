import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";
import "bootstrap/dist/css/bootstrap.min.css";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import Sidebar from "./sidebar/Sidebar";
import 'bootstrap/dist/css/bootstrap.css';
import React, { useEffect, useState } from "react";
import axiosInstance from "./axios";
import { Button } from "react-bootstrap";
import "./style.css"

const ApproveAccount= () => {
    const [accountHistory,setAccountHistory] = useState([]);

    useEffect( () => {
       axiosInstance.post("accounts/history")
        .then(res => {
            const val = res.data;
            setAccountHistory(val);
        })
        .catch(error => {
            console.log(error);
        });
    }, []);


    const  makeApprove= (cell, row, rowIndex, formatExtraData) => {
        return (
            <Button
            variant="success"
                onClick={() => {
                    let approving = {
                        username: row.username,
                        numberAccount: row.numberAccount,
                        currency: row.currency,
                        accountStatus: row.accountStatus,
                        currentStatus:row.currentStatus,
                        executor:localStorage.getItem("USER_USERNAME")
                    }
                            
                axiosInstance.post("accounts/handle", approving)
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
            dataField: "username",
            text: "Username",
            
        },

        {
                
            dataField: "numberAccount",
            text: "AccountNumber",
            
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
            text: "CurrentStatus"
        },

        {
            dataField: "lastStatus",
            text: "LastStatus"
        },

        {
            dataField: "executor",
            text: "Executor"
        },

        {
            text: "Approve", 
            formatter: (cell, row, rowIndex, formatExtraData) => {
                return makeApprove(cell, row, rowIndex, formatExtraData);
            } 
        },
      

        ];
     

    
          
   
        return (
        <div>
          <Sidebar />
            <div className="container" style={{ padding: "20px" }}>
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={accountHistory}
                    columns={columns}
                    pagination={paginationFactory({ sizePerPage: 5 })}
        
                    
                />
            </div>
        </div>
        );
    
 }
export default ApproveAccount;