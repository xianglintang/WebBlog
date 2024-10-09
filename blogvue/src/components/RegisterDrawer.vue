<template>
  <el-drawer
    title="注册"
    v-model="drawerVisible"
    direction="rtl"
    size="50%"
    @close="resetForm"
  >
    <!-- 留言表单 -->
    <el-form 
      :model="form" 
      :rules="RegisterRule"
      ref="RegisterFromRef"
      label-width="30%"
      >
        <el-form-item label="账号" prop="adminName">
          <el-input v-model="form.adminName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="adminPassword">
          <el-input type="password" show-password v-model="form.adminPassword"></el-input>
        </el-form-item>
        <el-form-item label="再次输入密码" prop="adminPasswordAgain">
          <el-input type="password" show-password v-model="form.adminPasswordAgain"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="邮箱验证码" prop>
          <el-input style="width: 70%;" v-model="form.emailCode"></el-input>
          <el-button
          type="primary"
          style="width: 30%;" 
          :disabled="isCounting"
          @click="BeforesendEmailCode"
          >{{ isCounting ? `${countdown}s` : '发送验证码' }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFrom">注册</el-button>
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
import {SendCode,VerifyCode} from '@/api/mail.js'
import {CheckAdminNameIsExist,CheckEmailIsExist,Register} from '@/api/admin.js'
import { ElMessage } from "element-plus";
import Verify from "@/components/verifition/Verify.vue";
import { v4 as uuidv4 } from "uuid";
import debounce from 'lodash/debounce';

// 图片验证码
const verify = ref();//我们ref绑定<Verify>标签，获取它的实例，然后通过verify.value.show()内置方法，展示验证码
const RegisterFromRef = ref();
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
    adminName: '',
    adminPassword: '',
    adminPasswordAgain: '',
    email:'',
    emailCode:'',
    identifier:"",//唯一标识
  }
);
//adminPasswordAgain自定义规则
const validatePasswordAgain = async (rule, value, callback) => {
  // rule: 验证规则
  // value: 当前字段的值
  // callback: 回调函数，用于返回验证结果
 if (value != form.adminPassword) { 
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();  // 验证通过，调用 callback() 无参数表示通过
  }
};
const validateAdminName = async (rule, value, callback) => {
  try {
    // 假设这是调用后端 API 检查 adminName 是否唯一的请求
    const response = await CheckAdminNameIsExist(value);
    if (response.data.adminNameIsExist) {
      console.log(666);
      callback(new Error('账号已存在，请选择其他账号'));
    }
    else{
      callback();  // 验证通过
    }
  } catch (error) {
    callback(new Error('验证账号时出错，请稍后再试'));
  }
};

const validateEmail = async (rule, value, callback) => {
  try {
    // 假设这是调用后端 API 检查 email 是否唯一的请求
    const response = await CheckEmailIsExist(value);

    if (response.data.emailIsExist) {
      callback(new Error('该邮箱已注册，请使用其他邮箱'));
    } else {
      callback();  // 验证通过
    }
  } catch (error) {
    callback(new Error('验证邮箱时出错，请稍后再试'));
  }
};
//定义非空规则,什么都可以空，但是信息不可以为空
const RegisterRule = reactive({
  adminName: [{ required: true, message: "账号不能为空", trigger: "blur" },{ validator: validateAdminName, trigger: 'blur' }],
  adminPassword: [{ required: true, message: "密码不能为空", trigger: "blur" },],
  adminPasswordAgain: [{ required: true, message: "请再次输入密码", trigger: "blur" },{ validator: validatePasswordAgain, trigger: 'blur' }],
  email: [{ required: true, type: "email", message: "请输入有效的邮箱地址", trigger: "blur" },{ validator: validateEmail, trigger: 'blur' }],
});


// 倒计时控制
const isCounting = ref(false);
const countdown = ref(60);
let countdownInterval;

// 验证码倒计时
const startCountdown = () => {
  isCounting.value = true;
  countdown.value = 60;
  countdownInterval = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(countdownInterval);
      isCounting.value = false;
    }
  }, 1000);
};
//发送邮箱验证码之前
const BeforesendEmailCode = () =>{
  if (!RegisterFromRef) return;
  RegisterFromRef.value.validate((valid) => {//规则验证
    if (valid) { 
      verify.value.show();
    }else {
      return false;
    }
  })
};

// 发送邮箱验证码
const sendEmailCode = () => {
  // 生成唯一标识符（UUID）
  const identifier = uuidv4();
  console.log("sessionStorage-Identifier:"+sessionStorage.getItem("Identifier"));
  // 保存标识符到 sessionStorage,先判定是否已经存在，避免再生成
  if(sessionStorage.getItem("Identifier") == null){
    sessionStorage.setItem("Identifier", identifier);
    form.identifier = identifier;
    console.log("不存在唯一值");
  }
  else{
    form.identifier = sessionStorage.getItem("Identifier");
    console.log("已存在唯一值");
  }

  // 发送邮箱验证码请求，同时发送标识符
  SendCode(form).then((resp) => {
    if (resp.code === 200) {
      ElMessage.success("验证码已发送");
      startCountdown();
    } else {
      ElMessage.error(resp.msg);
    }
  });
};

// 验证码校验通过,拼图验证码有两次，一次是发验证码邮件验证前，一次就是账号密码登录，使用验证码不用再拼图验证码
const successVerify = (param) => {
  ElMessage.success("图片验证码校验通过");
  // 发起登录请求
    sendEmailCode();
};

// 关闭抽屉
const closeDrawer = () => {
  emit('update:drawerVisible', false)
  console.log("取消然后关闭了");
};

//提交
const submitFrom=()=> {
  try {
    console.log("提交的信息："+form);
    //开启验证码
    if(!RegisterFromRef) return;
    RegisterFromRef.value.validate((valid)=>{//检验规则是否合规
      if(valid){
        //手动验是否空
        if(form.emailCode == "" || form.emailCode==null) return ElMessage.error("验证码不能为空");
        VerifyCode(form).then((resp) => {
          if (resp.code === 200) {
            //ElMessage.success("验证码验证成功");
            //符合规则,不用再滑动验证了，直接注册
            Register(form).then((resp) =>{
              if(resp.code === 200) {
                ElMessage.success("注册成功");
              }
            })
          } else {
            ElMessage.error(resp.msg);
          }
        });
      }else{
        return false;
      }
    });
  } catch (error) {
    ElMessage.error('提交失败');  
  }
};

// 重置表单
const resetForm = () => {
  form.name = '';
  form.contact = '';
  form.message = '';
  emit('update:drawerVisible', false)//右子组件为父组件传递值，让父组件来关闭，解决watch无法检测相同值的问题
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

