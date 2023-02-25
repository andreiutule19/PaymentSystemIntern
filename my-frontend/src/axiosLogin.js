import axios from "axios"

const axiosLogin = axios.create({
    baseURL: "http://localhost:8080/"
    
    
});

export default axiosLogin;