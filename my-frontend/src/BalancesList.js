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
import axios from "axios";

const BalancesList = () => {
    const [balances, setBalances] = useState([]);
    const history = useNavigate();
   


    
    useEffect(() => {

        axiosInstance.get("balances")
            .then(res => {
                const val = res.data;
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
          <Sidebar />
            <div className="container" style={{ padding: "20px" }}>
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={balances}
                    columns={columns}
                    pagination={paginationFactory({ sizePerPage: 5 })}
                    
                />
            </div>
        </div>
        );
    
 }
export default BalancesList;