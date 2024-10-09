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

//注册
export function Register(data){
    let param = {
        url:"/admin/register",
        method:"POST",
        data:qs.stringify(data)
    }
    return request(param);
}

//上传图片,放此，是因为上传图片只能是编辑文章的时候，编辑文章认为应该最需要权限，也就是登录
export function UploadImageGetURL(data){
    let param = {
        url:"/admin/file/upload",
        method:"POST",
        data:data,
        headers: {
            'Content-Type': 'multipart/form-data',
          },
    }
    return request(param);
}

//验证注册用户名是否唯一
export function CheckAdminNameIsExist(adminName) {
    // 使用 URLSearchParams 来构建查询参数
    const params = new URLSearchParams();
    params.append('adminName', adminName);
    let param = {
        url: "/admin/register/adminNameIsExist", // 确保路径正确
        method: "POST",
        data: params, // 将查询参数作为数据传入
    }
    return request(param);
}

//验证注册用户邮箱是否唯一
export function CheckEmailIsExist(email){
    const params = new URLSearchParams();
    params.append('email', email);
    let param = {
        url:"/admin/register/emailIsExist",
        method:"POST",
        data:params,
    }
    return request(param);
}



