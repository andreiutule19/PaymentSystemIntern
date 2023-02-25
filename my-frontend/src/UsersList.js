
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

const UsersList = () => {
    const [users, setUsers] = useState([]);
    const history = useNavigate();
   


    
    useEffect(() => {
        axios.get("http://localhost:8080/users", {
            headers: {
                Authorization:"Bearer " + localStorage.getItem("token")
            }
        })
        // axiosInstance.get("users")
            .then(res => {
                const val = res.data;
                setUsers(val);
            })
            .catch(error => {
                console.log(error);
            });
    },[]);

    
    const  makeEdit = (cell, row, rowIndex, formatExtraData) => {
        return (
          <Button
                onClick={() => {
                    console.log(row);
                
                    history('/edit', {
                        state: {
                            username: row.username,
                            email: row.email,
                            fullName: row.fullName,
                            address: row.address,
                            status:row.status
                        }
                    });
            }}
          >
            Edit
          </Button>
        );
    };


    const  makeDelete = (cell, row, rowIndex, formatExtraData) => {
        return (
            <Button
            variant="danger"
                onClick={() => {

                    let userData = {
                        username:row.username,
                        email:row.email,
                        fullName:row.fullName,
                        address:row.address,
                        status:row.status,
                        executor:localStorage.getItem("USER_USERNAME")
                    
                    }    
                axiosInstance.post("users/remove", userData)
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
                
            dataField: "email",
            text: "Email",
            
        },

        {
            dataField: "fullName",
            text: "Full Name"

        },

        {
            dataField: "address",
            text: "Address"
        },

        {
            dataField: "status",
            text: "Status"
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
        }
      

        ];
   
        return (
        <div>
          <Sidebar />
            <div className="container">
                    <BootstrapTable
                    striped hover condensed
                    keyField="id"
                    data={users}
                    columns={columns}
                    pagination={paginationFactory({ sizePerPage: 5 })}
                    
                />
            </div>
        </div>
        );
    
 }
export default UsersList;