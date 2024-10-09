import request from "@/request/request";
import qs from "qs"

//上传文件
export function UploadFile(data,config = {}){
    let param = {
        url:"/file/upload",
        method:"POST",
        data:data,
        headers: {
            'Content-Type': 'multipart/form-data',
          },
        ...config
    }; 
    return request(param);
}