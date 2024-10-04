
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(),],
  resolve:{
    alias:{
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    open: true,//项目运行自动打开浏览器打开本项目网址
    port: 3000,
    proxy: {//代理，baseURL请求的路径，通过代理重写，解决跨域问题
      "/api": {
        target: "http://localhost:80", //代理目标地址
        changeOrigin: true,//改变请求来源，修改为后端请求路径，而非前端默认的localhost路径，避免被拒绝
        rewrite: (path) => path.replace(/^\/api/, ""),//在代理请求时，将请求路径中的 /api 前缀去掉。
        //例如，/api/users 会被重写为 /users，然后再发送到后端服务器。
      },
    },
  },
});

