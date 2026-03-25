import axios from 'axios';

// 建立一個 Axios 實體
const api = axios.create({
  // 設定後端 Spring Boot 的基礎網址 (預設通常是 8080 port)
  // 之後呼叫 API 就只要寫相對路徑，例如 '/users/login'
  baseURL: 'http://localhost:8080/api', 
  timeout: 10000, // 設定請求超時時間為 10 秒
  headers: {
    'Content-Type': 'application/json',
  }
});

// 請求攔截器 (Request Interceptor)
// 這裡預留給「登入驗證」使用，發送請求前會先經過這裡
api.interceptors.request.use(
  (config) => {
    // 假設未來你把登入的 token 存在 localStorage
    const token = localStorage.getItem('token');
    if (token) {
      // 如果有 token，就自動加到每個請求的 Header 中
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 回應攔截器 (Response Interceptor)
// 這裡可以用來統一處理後端回傳的錯誤，例如 Token 過期
api.interceptors.response.use(
  (response) => {
    return response.data; // 直接回傳資料部分，讓你在元件裡少寫一層 .data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      console.error('身分驗證失敗或 Token 過期，請重新登入');
      // 未來可以在這裡加入自動導向登入頁面的邏輯
    }
    return Promise.reject(error);
  }
);

export default api;