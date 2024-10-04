<template>
  <div>
    <el-menu
        :default-active="$route.path"
        mode="horizontal"
        :ellipsis="false"
        router
        :background-color="'#f5f5f5'"
        :text-color="'#000000'"
        :active-text-color="'#000000'"
        :default-openeds="['/title']"
        :collapse="false"
        :unique-opened="false"
    >
    <!--   style="position: fixed"-->
      <el-menu-item index="/home"><template #title>主页</template></el-menu-item>
      <div style="margin: auto;">
        <span style="margin: auto;">文章搜索：</span>
        <el-input v-model="SearchValue"
                  style="width: 250px;height: 30px;margin: auto;"
                  placeholder="Please input title"
                  :prefix-icon="Search"
                  clearable
                  @keyup.enter="SearchTitle">
        </el-input>
        <el-button type="primary" style="margin: auto;" @click="SearchTitle">搜索</el-button>
      </div>
      <el-menu-item index="/article_category"><template #title>文章类型</template></el-menu-item>
      <el-menu-item index="/about"><template #title>关于</template></el-menu-item>
      <el-menu-item :index="loginMenuIndex">
        <template #title>{{statusName}}</template>
      </el-menu-item>
    </el-menu>
  </div>
  <router-view></router-view>
</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import {watch, ref, onMounted,computed} from 'vue';
import { storeToRefs } from 'pinia';
import router from "@/router/index.js";
import { useSystemStore } from '@/stores/system.js';

//登录
const store = useSystemStore();//更改登录为账号名
const token = sessionStorage.getItem('token');
const statusName=ref('登录');
const adminInfo = JSON.parse(sessionStorage.getItem("adminInfo"));
//console.log("app:"+adminInfo.adminName);

onMounted(() => {
  return token ?  statusName.value = adminInfo.adminName: statusName.value = '登录';
});
watch(() =>adminInfo,(newvalue)=> {
  console.log("watch");
  statusName.value = newvalue;
  router.go(0);
});

// 登录菜单的 index
const loginMenuIndex = computed(() => {
  return token ? '/admin' : '/login';
});


//搜索
const SearchValue = ref('');
const SearchTitle = () => {
  console.log("SearchValue.value:"+SearchValue.value)

  router.push({
    name: 'search',//路由地址别名,转区的地址
    query: { SearchValue: SearchValue.value },
  });
}




</script>
<style scoped>

el-menu-item{
  width: 50px;
  height: 50px;
}
</style>
