<template>
  <div>admin</div>
  <el-button type="primary" @click="logoutd">退出登录</el-button>

  <el-button type="primary" @click="editArticle">编写新文章</el-button>
</template>

<script setup>
import {watch, ref, onMounted,computed} from 'vue';
import { useSystemStore } from '@/stores/system.js';
import { ElMessage } from "element-plus";
import router from "@/router/index.js";


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

//编写文章
const editArticle = () =>{
  router.push({
    name: 'articleEditor',//路由地址别名,转区的地址
  });
}
</script>

<style scoped>
.upload-demo {
  width: 80%;
}

</style>