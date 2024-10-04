import { ref, reactive, computed } from 'vue';
import { defineStore } from 'pinia';

export const useSystemStore = defineStore('system', () => {

  const collapse = ref(false);   // 导航栏收缩状态

  const adminInfo = ref({});      // 登录的用户信息

  //const adminName = ref('登录');//不持久，刷新重置没必要
    //console.log(adminInfo.value.adminName);//对象引用例子
  // 初始化用户信息
  const initAdminInfo = (adminInfoParam) => {
    adminInfo.value = adminInfoParam;
  };

  // 改变收缩状态
  const toggleCollapse = () => {
    collapse.value = !collapse.value;
  };

  // 退出登录
  const logout = () => {
    adminInfo.value = {};
  };
  
  return { collapse, adminInfo, initAdminInfo, toggleCollapse, logout };
});
