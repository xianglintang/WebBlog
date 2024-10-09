<template>
  <div>about 页面</div>
  <br/>
  <div class="container">
    <!-- 留言按钮 -->
    <el-button type="primary" @click="MessageDrawerVisible = true" class="MessageButton">留言</el-button>
    <!-- 引入留言抽屉组件 -->
    <MessageDrawer v-model:drawerVisible="MessageDrawerVisible"  @update:drawerVisible="MessageDrawerVisible = $event"/>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue';
import MessageDrawer from '@/components/MessageDrawer.vue';
import router from "@/router/index.js";
import {UploadFile} from "@/api/test.js";

const MessageDrawerVisible = ref(false); // 控制留言抽屉的显示与隐藏
const handleVerified = () => {
  showCaptcha.value = false;
  submitMessage();
};
const submitMessage = () => {
  // 在这里进行提交留言的逻辑
  console.log('验证通过，留言已提交');
};

</script>

<style scoped>
.container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  height: 50vh; /* 让容器高度为视口高度 */
}
.MessageButton{
  margin:auto;
}
</style>
