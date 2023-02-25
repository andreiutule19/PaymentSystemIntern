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

const PaymentList = () => {
    const [payment, setPayment] = useState([]);
    const history = useNavigate();
   


    
    useEffect(() => {
        axiosInstance.get("payments")
            .then(res => {
                const val = res.data;
                setPayment(val);
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

        
       
      

        ];
   
        return (
        <div>
          <Sidebar />
            <div className="container" style={{ padding: "20px" }}>
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={payment}
                    columns={columns}
                    pagination={paginationFactory({ sizePerPage: 5 })}
                    
                />
            </div>
        </div>
        );
    
 }
export default PaymentList;