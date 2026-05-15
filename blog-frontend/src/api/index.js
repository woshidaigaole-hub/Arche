import axios from 'axios'

/**
 * Axios 封装 —— 统一管理所有 HTTP 请求
 * 对照 Java：相当于一个封装了 HttpClient 的工具类
 */

const api = axios.create({
  baseURL: '/api',       // 所有请求自动加 /api 前缀
  timeout: 10000         // 10秒超时
})

// 请求拦截器：每次发请求前，自动把 token 加到请  求头
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一处理错误，比如 401 就跳到登录页
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

// ---------- API 方法，每个对应后端一个接口 ----------

export const articleApi = {
  list:     ()           => api.get('/articles'),
  detail:   (id)         => api.get(`/articles/${id}`),
  create:   (data)       => api.post('/articles', data),
  update:   (id, data)   => api.put(`/articles/${id}`, data),
  remove:   (id)         => api.delete(`/articles/${id}`),
}

export const authApi = {
  login:    (data)       => api.post('/auth/login', data),
  register: (data)       => api.post('/auth/register', data),
}

export default api
