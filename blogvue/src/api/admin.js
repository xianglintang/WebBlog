import request from "@/request/request";
import qs from "qs"

/**
 * 用户登录
 * @param loginInfo
 * @returns {Promise<AxiosResponse<any>>}
 */
//登录
export function Login(loginInfo){
    let param = {
        url:"/admin/login",
        method:"POST",
        data:qs.stringify(loginInfo)
    }
    //console.log(data);
    return request(param);
}

//邮箱验证
export function LoginMail(loginInfo){
    let param = {
        url:"/admin/verifycode",
        method:"POST",
        data:qs.stringify(loginInfo)
    }
    //console.log(data);
    return request(param);
}

//退出登录
export function Logout(data){
    let param = {
        url:"/admin/logout",
        method:"get"
    }
    return request(param);
}







