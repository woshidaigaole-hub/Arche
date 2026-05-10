import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      // 开发时 /api 请求转发到后端 8080，解决跨域
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      // 上传的图片也通过代理访问 8080
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
