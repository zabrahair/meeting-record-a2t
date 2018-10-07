import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import { getToken } from 'utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 1000 * 60// 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  if (store.getters.token) {
    config.headers['X-Token'] = getToken() // 让每个请求携带token--['X-Token']为自定义key 请根据实际情况自行修改
  }
  if (config.method === "post") {
    config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
    config.transformRequest = function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }
  }
  return config
}, error => {
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => response,

  error => {
    const res = error.response;
    if (res.status !== 200) {
      if (res.status == 500) {
        Message({
          message: res.data.message,
          type: "error",
          duration: 5 * 1000
        })
      }
      if (res.status > 500) {
        Message({
          message: "服务器错误",
          type: "error",
          duration: 5 * 1000
        })
      }
      if (res.status === 401) {
        Message({
          message: "登录信息过期，请重新登录",
          type: 'error',
          duration: 2 * 1000
        })
        store.dispatch('FedLogOut').then(() => {
          location.reload();// 为了重新实例化vue-router对象 避免bug
        })
      }
      if (res.status === 404) {
        Message({
          message: "请求接口错误",
          type: "error",
          duration: 5 * 1000
        })
      }
    }
    return Promise.reject(error);
  }
)

export default service
