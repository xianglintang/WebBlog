<template>
  <el-drawer
    title="留言"
    v-model="drawerVisible"
    direction="rtl"
    size="40%"
    @close="resetForm"
  >
    <!-- 留言表单 -->
    <el-form 
      :model="form" 
      :rules="MessageRule"
      ref="MessageFromRef"
      label-width="80px"
      >
        <el-form-item label="姓名" >
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.contact"></el-input>
        </el-form-item>
        <el-form-item label="留言" prop="message">
          <el-input type="textarea" v-model="form.message"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitMessage">提交留言</el-button>
          <el-button @click="closeDrawer">取消</el-button>
        </el-form-item>
    </el-form>
  </el-drawer>
  <!--引入滑动拼图验证码, :mode="'pop'"弹窗-->
  <Verify
  @success="successVerify"
  :mode="'pop'"
  :captcha-type="'blockPuzzle'"
  :imgSize="{ width: '400px', height: '200px' }"
  ref="verify"
  class="verify-overlay"
  ></Verify>
</template>

<script setup>
import {ref, reactive,watch, defineEmits} from 'vue'
import {SendMail} from '@/api/mail.js'
import { ElMessage } from "element-plus";
import Verify from "@/components/verifition/Verify.vue";

// 图片验证码
const verify = ref();//我们ref绑定<Verify>标签，获取它的实例，然后通过verify.value.show()内置方法，展示验证码
const MessageFromRef = ref();
const props = defineProps({
  drawerVisible: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:drawerVisible'])

const drawerVisible = ref(props.drawerVisible);

watch(() => props.drawerVisible, (newvalue) => {
  drawerVisible.value = newvalue
  console.log("组件内drawerVisible.value:"+drawerVisible.value);
})
const form = reactive(
  { 
    name: '',
    contact: '',
    message: ''
  }
);

//定义非空规则,什么都可以空，但是信息不可以为空
const MessageRule = reactive({
  message: [
    { required: true, message: "留言信息不能为空", trigger: "blur" },
    {min: 5, max: 10000,  message: '至少5个字且最多一万字',trigger: 'blur' }
  ]
});

// 关闭抽屉
const closeDrawer = () => {
  emit('update:drawerVisible', false)
  console.log("取消然后关闭了");
};

//提交
const submitMessage=()=> {
  try {
    console.log("提交的信息："+form);
    //开启验证码
    if(!MessageFromRef) return;
    MessageFromRef.value.validate((valid)=>{//检验规则是否合规
      if(valid){
        //符合规则
        verify.value.show();//展示验证码，后续提交转到验证码成功后
      }else{
        return false;
      }
    });
  } catch (error) {
    alert('提交失败');  
  }
};

// 重置表单
const resetForm = () => {
  form.name = '';
  form.contact = '';
  form.message = '';
  emit('update:drawerVisible', false)//右子组件为父组件传递值，让父组件来关闭，解决watch无法检测相同值的问题
};

//验证码
// 验证码校验通过
const successVerify = async(param) => {
  ElMessage.success("图片验证码校验通过");
  // 发起提交请求
  await SendMail(form).then((resp) => {
    console.log(resp);
    ElMessage.success('留言已提交'); // 使用原生 alert 或者用 ElementUI 的消息提示组件
    emit('update:drawerVisible', false); // 提交后关闭抽屉
  }).catch((error) => {
    ElMessage.error(error.msg);
  });
};


</script>

<style scoped>
.verify-overlay {
  position: fixed; /* 确保覆盖整个视口 */
  top: 0;
  left: 0;
  z-index: 10000; /* 确保它在其他所有元素上方,数字代表优先级，也就是堆叠数 */
}

</style>

