import axios from 'axios';
const interceptor = axios.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

export default interceptor; // Xuất interceptor để sử dụng trong các mô-đun khác
