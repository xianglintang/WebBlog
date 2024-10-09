import request from "@/request/request";
import qs from "qs"

//搜索和模糊搜索的
export function Search(data){
    let param = {
        url:"/article",
        method:"GET",
        params:data
    }
    return request(param);
}

//这个通过id获取到后，要后端将md文件转为html传输到前端
export function SearchById(id){
    let param = {
        url:"/article/"+id,
        method:"GET",
    }
    return request(param);
}
export function GetArticleByIdNoHTML(id){
    let param = {
        url:"/article/nohtml/"+id,
        method:"GET",
    }
    return request(param);
}
//title那边按类别的
export function SearchByCategory_Id(id){
    let param = {
        url:"/article/category/"+id,
        method:"get"
    }
    return request(param);
}

//上传文章
export function UploadArticle(data,config = {}){
    let param = {
        url:"/article/uploadArticle",
        method:"POST",
        data:data,
        headers: {
            'Content-Type': 'multipart/form-data',
          },
        ...config
    }; 
    return request(param);
}
//修改文章
export function UpdateArticle(data,config = {}){
    let param = {
        url:"/article/update",
        method:"POST",
        data:data,
        headers: {
            'Content-Type': 'multipart/form-data',
          },
        ...config
    }; 
    return request(param);
}






