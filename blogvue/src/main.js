import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router';
import home from './views/blog/home.vue';
import App from './App.vue'
import locale from 'element-plus/dist/locale/zh-cn'
import highlight from '@/utils/highlight.js';
import { createPinia } from 'pinia';
//const app = createApp(App)
createApp(App).directive('highlight', highlight).use(ElementPlus,{locale}).use(router).use(createPinia()).mount('#app');