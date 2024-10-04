import axios from 'axios';

//这个应该是演示的，baseURL不是寻常路径，而是某个网站，应该是不用做后端只前端来演示是否正常的
//所以这个文件不应该调用，而是调用自己的request.js或者axios.js
axios.defaults.baseURL = 'https://captcha.anji-plus.com/captcha-api';

const service = axios.create({
  timeout: 40000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=UTF-8'
  },
})
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data;
    return res
  },
  error => {
  }
)
export default service
