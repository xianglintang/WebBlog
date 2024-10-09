import request from "@/request/request";
import qs from "qs"

//将留言信息传到后端，然后后端处理发邮件
export function SendMail(data){
    let url = "/api/mail/send";
    let param = {
        url:url,
        method:"POST",
        data:data
    }
    return request(param);
}
//邮箱验证码发送
export function SendCode(data){
    let url = "/api/mail/sendcode";
    let param = {
        url:url,
        method:"POST",
        data:qs.stringify(data)
    }
    return request(param);
}

//邮箱验证码验证,这个是注册的，与登录的不同，没有设置token也没有返回数据
export function VerifyCode(data){
    let url = "/api/mail/verifycode";
    let param = {
        url:url,
        method:"POST",
        data:qs.stringify(data)
    }
    return request(param);
}
