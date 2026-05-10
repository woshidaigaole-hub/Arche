import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'

/**
 * Vue 应用入口 —— 三板斧：
 *   1. createApp(App) 创建应用实例
 *   2. .use(router)   挂载路由插件
 *   3. .mount('#app')  挂载到 index.html 的 <div id="app">
 */
createApp(App).use(router).mount('#app')
