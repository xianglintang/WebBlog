import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const instance = axios.create({
    baseURL: '/api',//转去配置文件里面的伪装后端
    //withCredentials: true
    /*headers: {
        'Content-Type': 'application/json' // 添加请求头
    }, *///登录不可以使用这个表头，不然后端读取不到 
    // 超时时间
    timeout: 3000,
})


// 添加请求拦截器
instance.interceptors.request.use(
    (config)=> {
        let token = sessionStorage.getItem('token')
        if (token) {
            config.headers['token'] = token
        }
        // 在发送请求之前做些什么*/
        return config
    },
    (error)=> {
        // 对请求错误做些什么
        return Promise.reject(error)
    }
)

// 添加响应拦截器
instance.interceptors.response.use(
    response => {
        // 对响应数据做点什么
        return response.data;//这个让前端，不再respond.data.data而是respond.data就可以了
    },
    error => {
        //没有登录、登录过期了//&& error.response.status == 401
        if (error.response) {
            //window.location.href="http://localhost:8080/login";基本用路由而不是超链接
            /*router.replace('/login')禁用先
            return error.response.data*/
            const status = error.response.status
            switch (status) {
                case 400:
                ElMessage.error("请求错误");
                break;
                case 401:
                ElMessage.error("未授权，请重新登录");
                router.replace('/login');
                break;
                case 403:
                ElMessage.error("登录过期，请重新登录");
                router.replace('/login');
                break;
                case 404:
                ElMessage.error("请求错误，未找到相应的资源");
                break;
                case 408:
                ElMessage.error("请求超时");
                break;
                case 500:
                ElMessage.error("服务器错误");
                break;
                case 501:
                ElMessage.error("网络未实现");
                break;
                case 502:
                ElMessage.error("网络错误");
                break;
                case 503:
                ElMessage.error("服务不可用");
                break;
                case 504:
                ElMessage.error("网络超时");
                break;
                case 505:
                ElMessage.error("HTTP版本不支持该请求");
                break;
                default:
                ElMessage.error("请求失败");
            }
        } else {
            if (JSON.stringify(error).includes("timeout")) {
              error.code = "TIMEOUT";
              error.message = "服务器响应超时，请刷新页面";
            }
        }
        // 对响应错误做点什么
        return Promise.reject(error)

    }
)
//导出
export default function request(param) {
    return instance(param)
}