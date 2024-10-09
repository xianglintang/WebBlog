<template>
  <div id="vditor"></div>
  <!--文章信息-->
  <br>
  <div>
    <el-form ref="articleformRef" :model="formData" :rules="articleRules" label-width="100px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入标题" />
      </el-form-item>

      <el-form-item label="介绍" prop="introduce" >
        <el-input type="textarea" v-model="formData.introduce" placeholder="请输入介绍" />
      </el-form-item>

      <el-form-item label="分类"> 
        <el-select v-model="formData.categoryId" placeholder="请选择文章分类" >
          <el-option label="默认: spring" value="1" />
          <el-option label="unity" value="2" />
          <el-option label="csharp" value="3" />
          <el-option label="c" value="4" />
          <el-option label="python" value="5" />
          <el-option label="other" value="6" />
        </el-select>
      </el-form-item>

        <!-- md文件上传自动解析到编辑页面里面 -->
        <el-form-item label="内容" prop="file" style="display: flex;flex-direction: row;">
        <el-upload
            style="display: flex;align-items: center;"
            action="" 
            :file-list="fileList"
            :on-change="handleFileChange"
            :before-upload="beforeUpload"
            :limit="1"
            :auto-upload="false"
        >
            <el-button >md文本导入</el-button>
        </el-upload>
        <el-tooltip effect="dark" style="display: flex;align-items: center;"
              content="导入外部md文件，进行在线编辑即可上传。文件大小不能超过 10MB，且只能上传一个文件。
               导入的文件内容会拼接在原本编辑页面内数据的后面，如果想拼接多个文件，可以将旧文件删掉，导入新文件即可"
              placement="top">
              <el-button size="small" >?</el-button>
            </el-tooltip>
        </el-form-item>
        <!-- 提交按钮 -->
        <el-form-item>
        <el-button type="primary" @click="submitForm">保存提交</el-button>
        <!--<el-button @click="resetForm">重置</el-button>-->
      </el-form-item>
      <!-- 弹窗显示上传进度 -->
      <el-dialog
          title="上传中"
          v-model="isUploading"
          width="400px"
          :show-close="false"
        >
          <div style="text-align: center;">
            <el-progress :percentage="uploadProgress" status=""></el-progress>
            <div style="margin-top: 20px;">
              {{ uploadProgress }}%
            </div>
          </div>
        </el-dialog>
    </el-form>
  </div>
</template>
<script setup>
import { ref, onMounted ,reactive} from "vue";
import { useRoute } from 'vue-router';
import Vditor from "vditor";
import "vditor/dist/index.css";
import { ElMessage } from "element-plus";
import {UploadImageGetURL} from "@/api/admin.js";
import {UploadArticle,GetArticleByIdNoHTML,UpdateArticle} from "@/api/article.js";
import { v4 as uuidv4 } from 'uuid'; 

const route = useRoute();

const articleformRef = ref();
const articleRules = reactive({
  title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
  introduce: [{ required: true, message: "介绍不能为空", trigger: "blur" }],
});


//文章保存，也就是上传
//上传文件：
let adminInfo = JSON.parse(sessionStorage.getItem("adminInfo"));
// 上传进度
const uploadProgress = ref(0);
// 控制上传弹窗的可见性
const isUploading = ref(false);
const articleId = ref(route.query.articleId || 0); //文章id，如果是0就是新的编写，如果非0，那么就算编辑旧文章
const formData = ref({
  fileName:'',
  title: '',
  introduce: '',
  //file: null,//后面提交再额外弄
  adminId:adminInfo.adminId,
  categoryId: '1',//默认值1
});
const fileList = ref([]); 

//文章编写
const contentEditor = ref("");
onMounted(() => {
  contentEditor.value = new Vditor("vditor", {
    height: 360,
    toolbarConfig: {
      pin: true,
    },
    cache: {
      enable: false,
    },
    upload: {
      // 这里自定义上传图片的逻辑
      async handler(files) {
        for (let i = 0; i < files.length; i++) {//多次上传，将全部一一获取
          const formData = new FormData();
          formData.append("file", files[i]);//一次上传的数量，取第一个，如果一次上传一次，粘贴肯定是一次一个，所以符合
          //console.log(formData.get("file"));
          try {
            const response = await UploadImageGetURL(formData);
            // 返回 OSS 图片链接后插入到 Vditor 
            const ossUrl = response.data;
            contentEditor.value.insertValue(`![image](${ossUrl})`);
            //contentEditor.value.insertValue(`![image](${formData.get("file").name})`);//测试
          } catch (error) {
            console.error("图片上传失败", error);
            ElMessage.error('图片上传失败',error);

          }
        }
      },
      accept: "image/*", // 仅接受图片文件
    },
    after: () => {
      if (articleId.value != 0) {
        console.log(666);
        fetchArticleData(articleId.value); // 如果是编辑，获取文章内容
      } else {
        contentEditor.value.setValue("# hello,Vditor+Vue! 测试导入和显示");
      }
      //console.log(contentEditor.value.getValue());
      //console.log(uuidv4());
    },
  });
});

// 获取文章数据
const fetchArticleData = async (id) => {
  try {
    const response = await GetArticleByIdNoHTML(id);//传来标题，介绍，类型id
    console.log(response);
    console.log(response.data);
    const mdContent = response.data.fileNoHtml; // 假设返回的 md 内容
    contentEditor.value.setValue(mdContent); // 设置内容到编辑器
    // 更新表单数据
    formData.value.fileName = response.data.fileName;
    console.log(formData.value.fileName );
    formData.value.title = response.data.title;
    formData.value.introduce = response.data.introduce;
    console.log(formData.value.categoryId);
    formData.value.categoryId = response.data.categoryId;
    console.log(formData.value.categoryId);
  } catch (error) {
    ElMessage.error('获取文章内容失败');
  }
};

// 文件大小限制为 10MB
const beforeUpload = (file) => {
   // 检查文件类型和大小等
  const isMarkdown = file.type === 'text/markdown' || file.name.endsWith('.md');
  const isLt10M = file.size / 1024 / 1024 < 10; // 小于10MB
  if (!isMarkdown) {
   ElMessage.error('上传文件只能是 Markdown 格式!');
  }
  if (!isLt10M) {
    ElMessage.error('上传文件大小不能超过 10MB!');
  }
  return isMarkdown && isLt10M; // 返回 true 以允许上传
};

// 处理文件改变事件
const handleFileChange = (file, fileList) => {
  const reader = new FileReader();
  reader.onload = (event) => {//重写方法
    const markdownContent = event.target.result; // 获取文件内容
    contentEditor.value.setValue(contentEditor.value.getValue() + markdownContent); // 将内容赋值给 Vditor 实例
    //在旧数据后拼接，防止用户写的数据被覆盖而丢失
  };
  reader.readAsText(file.raw); // 读取文件为文本,异步，执行完，调用onload
};


const submitForm = async () => {
  //判断是否符合
  articleformRef.value.validate(async(valid) => {
    if(valid){
      const data = new FormData();
      //id是别处传过来的。
      data.append('articleId', articleId.value);
      data.append('title', formData.value.title);
      data.append('introduce', formData.value.introduce);
      data.append('categoryId', formData.value.categoryId);
      //文件初始为字符串，我们要转换为文件再复制
      // 将 Markdown 内容转换为 Blob 对象
      let blob = new Blob([contentEditor.value.getValue()], { type: "text/markdown" });

      if(articleId.value != 0){
        data.append('file', blob , formData.value.fileName);
      }else{
        data.append('file', blob , uuidv4()+'.md');
      }
      console.log(data.get('articleId'));
      console.log(contentEditor.value.getValue());
      console.log(data.get('file'));
      console.log(data.get('title'));
      console.log(data.get('categoryId'));
      try {
        // 打开上传进度弹窗
        isUploading.value = true;
        if(articleId.value != 0){
          //修改为主
          await UpdateArticle(data, {
            onUploadProgress: (progressEvent) => {
              const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
              uploadProgress.value = percentCompleted;
            },
          });
        }else{
          //上传为主
          await UploadArticle(data, {
            onUploadProgress: (progressEvent) => {
              const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
              uploadProgress.value = percentCompleted;
            },
        });
        }
        ElMessage.success('文章上传提交成功');
        isUploading.value = false;
        uploadProgress.value = 0;
      } catch (error) {
        ElMessage.error('文章上传提交失败');
      }
    }
    else{
      ElMessage.error('标题或者介绍不能为空');
    }

  });
};

const resetForm = () => {
    formData.value = {
      title: '',
      introduce: '',
      adminId:adminInfo.adminId,
      categoryId: '',
    };
      fileList.value = [];
};

</script>
<style scoped>
#vditor{
  height: 100%;
}

</style>

