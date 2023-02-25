import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

 const PrivateRoute = () => {
    return localStorage.getItem("USER_USERNAME") ? <Outlet /> : <Navigate to="/" />;
}
export default PrivateRoute;


