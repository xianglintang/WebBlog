<template>
  <div class="container">
    <el-form
      :model="loginForm"
      :rules="loginRule"
      size="large"
      ref="loginPasswordRef"
      class="login-form"
    >
      <el-form-item>
        <el-radio-group v-model="loginMode">
          <el-radio label="password">密码登录</el-radio>
          <el-radio label="email">邮箱验证码登录</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="loginMode === 'password'" prop="adminName">
        <el-input
          v-model="loginForm.adminName"
          placeholder="请输入账号"
          prefix-icon="UserFilled"
        />
      </el-form-item>
      <el-form-item v-if="loginMode === 'password'" prop="adminPassword">
        <el-input
          style="width: 75%;"
          show-password
          type="password"
          placeholder="请输入密码"
          v-model="loginForm.adminPassword"
          prefix-icon="Lock"
        >
        </el-input>
        <el-button
          style="width: 25%;"
          type="primary"
          @click="BeforesendEmailCode"
          >忘记密码?</el-button
        >
      </el-form-item>
      <el-form-item v-if="loginMode === 'email'" prop="email">
        <el-input
          v-model="loginForm.email"
          placeholder="请输入邮箱"
          prefix-icon="Message"
        />
      </el-form-item>
      <el-form-item v-if="loginMode === 'email'" prop="emailCode" >
        <el-input
          style="width: 70%;"
          v-model="loginForm.emailCode"
          placeholder="请输入验证码"
          prefix-icon="Lock"
        />
        <el-button
          style="width: 30%;"
          type="primary"
          :disabled="isCounting"
          @click="BeforesendEmailCode"
          >{{ isCounting ? `${countdown}s` : '发送验证码' }}</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="width: 100%"
          @click="loginSubmitCheck"
          >登录</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="width: 100%"
          @click="RegisterDrawerVisible = true"
          >注册</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 验证码 -->
    <Verify
      @success="successVerify"
      :mode="'pop'"
      :captcha-type="'blockPuzzle'"
      :imgSize="{ width: '400px', height: '200px' }"
      ref="verify"
      class="verify-overlay"
    ></Verify>
    <!--注册-->
    <RegisterDrawer v-model:drawerVisible="RegisterDrawerVisible"  @update:drawerVisible="RegisterDrawerVisible = $event"/>
  </div>
</template>
<script setup>
import { reactive, ref ,watch} from "vue";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import Verify from "@/components/verifition/Verify.vue";
import {Login,LoginMail} from "@/api/admin.js";
import {SendCode} from "@/api/mail.js"; 
import { useSystemStore } from '@/stores/system';
import { v4 as uuidv4 } from "uuid";
import RegisterDrawer from "@/components/RegisterDrawer.vue";

//关闭
// 重置表单
/*
const resetForm = () => {
  loginForm.adminName = '';
  loginForm.adminPassword = '';
};*/
//注册
const RegisterDrawerVisible = ref(false); // 控制注册抽屉的显示与隐藏

//登录
const store = useSystemStore();//保存token
const loginPasswordRef = ref();
const loginForm = reactive({
  adminName: "tangxianglin",
  adminPassword: "123456",
  email: "xianglintang@outlook.com",
  emailCode: "",
  identifier:"",//唯一标识
});
//v-if未渲染的，规则不会影响到
const loginRule = reactive({
  adminName: [{ required: true, message: "账号不能为空", trigger: "blur" }],
  adminPassword: [{ required: true, message: "密码不能为空", trigger: "blur" }],
  email: [{ required: true, type: "email", message: "请输入有效的邮箱地址", trigger: "blur" }],
  //emailCode: [{ required: true, message: "验证码不能为空", trigger: "blur" }],//这个应该可以手动
});

// 切换登录模式
const loginMode = ref("password");//默认密码

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
  if (!loginPasswordRef) return;
  loginPasswordRef.value.validate((valid) => {//规则验证
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
    loginForm.identifier = identifier;
    console.log("不存在唯一值");
  }
  else{
    loginForm.identifier = sessionStorage.getItem("Identifier");
    console.log("已存在唯一值");
  }

  // 发送邮箱验证码请求，同时发送标识符
  SendCode(loginForm).then((resp) => {
    if (resp.code === 200) {
      ElMessage.success("验证码已发送");
      startCountdown();
    } else {
      ElMessage.error(resp.msg);
    }
  });
};
//验证邮箱验证码

// 密码登录与邮箱验证码登录表单验证

// 图片验证码
const verify = ref();
// 登录前校验,
const loginSubmitCheck = () => {
  //邮箱验证码登录和密码登录共用
  if (!loginPasswordRef) return;
  loginPasswordRef.value.validate((valid) => {//规则验证
    if (valid) {
      if(loginMode.value === "email"){//邮箱验证码直接验证
        //手动验是否空
        if(loginForm.emailCode == "" || loginForm.emailCode==null) return ElMessage.error("验证码不能为空");
        //不空则开始验证码验证
        LoginMail(loginForm).then((resp) => {
          //console.log(resp);
          if (resp.code == 200) {
            console.log(resp.data.adminInfo);
            const adminInfo = resp.data.adminInfo;

            store.initAdminInfo(adminInfo);
            
            sessionStorage.setItem("token", resp.data.token);
            sessionStorage.setItem("adminInfo", JSON.stringify(adminInfo));
            //console.log();
            router.replace({ "path": "/admin" });//直接重置,会在request那边检测token从而自动转到admin
            ElMessage.success("登录通过");
          } else {
            ElMessage.error(resp.msg)
          }
        }).catch((error) => {
          ElMessage.error(error.msg);
        });
      }
      else{//这个是去验证账号密码的，提前滑动验证码再去验证
        verify.value.show();
      }
    } else {
      return false;
    }
  })
};

// 验证码校验通过,拼图验证码有两次，一次是发验证码邮件验证前，一次就是账号密码登录，使用验证码不用再拼图验证码
const successVerify = (param) => {
  ElMessage.success("图片验证码校验通过");
  // 发起登录请求
  //console.log("账号："+loginForm.adminName+",密码："+loginForm.adminPassword)
  if(loginMode.value === "email"){//如果是邮箱形式，就只发邮箱验证码即可
    sendEmailCode();
  }else{
    Login(loginForm).then((resp) => {
    //console.log(resp);
    if (resp.code == 200) {
      console.log(resp.data.adminInfo);
      const adminInfo = resp.data.adminInfo;

      store.initAdminInfo(adminInfo);
      
      sessionStorage.setItem("token", resp.data.token);
      sessionStorage.setItem("adminInfo", JSON.stringify(adminInfo));
      //console.log();
      router.replace({ "path": "/admin" });//直接重置,会在request那边检测token从而自动转到admin
      ElMessage.success("登录通过");
    } else {
      ElMessage.error(resp.msg)
    }
  }).catch((error) => {
    alert(error.msg);
  });
  }
  //resetForm();//关闭
  //重置网页
};

</script>
<style scoped>
.container{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 70vh;
  justify-content: center;
}
.login-form {
  width: 400px; /* 控制表单宽度 */
  padding: 20px; /* 内边距 */
  background-color: white; /* 设置背景色 */
  border-radius: 10px; /* 圆角 */
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.verify-overlay {
  position: fixed; /* 确保覆盖整个视口 */
  top: 0;
  left: 0;
  z-index: 10000; /* 确保它在其他所有元素上方,数字代表优先级，也就是堆叠数 */
}
</style>
