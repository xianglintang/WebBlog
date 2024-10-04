<template>
<div>admin</div>
<el-button type="primary" @click="logoutd">退出登录</el-button>
<div>
    <el-form ref="formRef" :model="formData" label-width="100px">
      <el-form-item label="标题">
        <el-input v-model="formData.title" placeholder="请输入标题" />
      </el-form-item>

      <el-form-item label="介绍">
        <el-input v-model="formData.introduce" placeholder="请输入介绍" />
      </el-form-item>

      <el-form-item label="分类">
        <el-select v-model="formData.categoryId" placeholder="请选择文章分类">
          <el-option label="spring" value="1" />
          <el-option label="unity" value="2" />
          <el-option label="csharp" value="3" />
          <el-option label="c" value="4" />
          <el-option label="python" value="5" />
          <el-option label="other" value="6" />
        </el-select>
      </el-form-item>

        <!-- 文件上传 -->
        <el-form-item label="内容" prop="file" required>
        <el-upload
            class="upload-demo"
            action="" 
            :file-list="fileList"
            :on-change="handleFileChange"
            :before-upload="beforeUpload"
            :limit="1"
            :auto-upload="false"
        >
            <el-button>选取文件</el-button>
            <div slot="tip" class="el-upload__tip">文件大小不能超过 10MB，且只能上传一个文件</div>
        </el-upload>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
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
import {watch, ref, onMounted,computed} from 'vue';
import { useSystemStore } from '@/stores/system.js';
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import {UploadArticle} from "@/api/article.js";
//退出登录
const store = useSystemStore();
const logoutd = () => {

    sessionStorage.removeItem("token");
    sessionStorage.removeItem("adminInfo")

    //store.commit("logout"); //vuex
    store.logout();
    //跳转到登录界面
    router.replace({ "path": "/login" });

}
//上传文件：

let adminInfo = JSON.parse(sessionStorage.getItem("adminInfo"));
// 上传进度
const uploadProgress = ref(0);
// 控制上传弹窗的可见性
const isUploading = ref(false);
const formData = ref({
  title: '',
  introduce: '',
  file: null,
  adminId:adminInfo.adminId,
  categoryId: '',
});
const fileList = ref([]); 

// 文件大小限制为 10MB
const beforeUpload = (file) => {
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
    this.$message.error('上传文件大小不能超过 10MB!');
    }
    return isLt10M;
};

const handleFileChange = (file) => {
  formData.value.file = file.raw;
};
const submitForm = async () => {
  const data = new FormData();
  data.append('title', formData.value.title);
  data.append('introduce', formData.value.introduce);
  data.append('file', formData.value.file);
  data.append('adminId', formData.value.adminId);
  data.append('categoryId', formData.value.categoryId);
  console.log(data.get('title'));
  console.log(data.get('categoryId'));
  try {
    // 打开上传进度弹窗
    isUploading.value = true;
    await UploadArticle(data, {
      onUploadProgress: (progressEvent) => {
        const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        uploadProgress.value = percentCompleted;
      },
    });
    ElMessage.success('文章上传成功');
    isUploading.value = false;
    uploadProgress.value = 0;
  } catch (error) {
    ElMessage.error('文章上传失败');
  }
};

const resetForm = () => {
    formData.value = {
      title: '',
      introduce: '',
      file: null,
      adminId:adminInfo.adminId,
      categoryId: '',
    };
      fileList.value = [];
    };
</script>

<style scoped>
.upload-demo {
  width: 80%;
}
</style>