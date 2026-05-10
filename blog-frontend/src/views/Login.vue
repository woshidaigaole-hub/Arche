<template>
  <div class="login">
    <div class="form-card">
      <h1 class="page-title">登录</h1>

      <div v-if="error" class="error">{{ error }}</div>

      <form @submit.prevent="handleLogin">
        <!-- v-model = 双向绑定：输入框值 和 JS变量 自动同步 -->
        <label class="label">用户名</label>
        <input v-model="form.username" class="input" type="text" required />

        <label class="label">密码</label>
        <input v-model="form.password" class="input" type="password" required />

        <button class="btn" type="submit" :disabled="loading" style="margin-top:24px;width:100%">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <p class="switch">
        没有账号？<router-link to="/register">注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api/index.js'

const router = useRouter()  // useRouter() = 在 JS 里控制页面跳转

// reactive() = 和 ref 类似，但专门给对象用，直接 .属性 访问
const form = reactive({ username: '', password: '' })
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  error.value = ''
  loading.value = true
  try {
    const res = await authApi.login(form)
    const data = res.data.data
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    router.push('/')  // 登录成功跳首页
  } catch (e) {
    error.value = e.response?.data?.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  padding-top: var(--space-xl);
}
.form-card {
  width: 100%;
  max-width: 380px;
}
.label {
  display: block;
  margin-top: var(--space-lg);
  margin-bottom: var(--space-xs);
  font-size: 13px;
  color: var(--color-text-soft);
  letter-spacing: 0.5px;
}
.error {
  margin-top: var(--space-md);
  padding: var(--space-sm) var(--space-md);
  background: #fef2f2;
  color: #991b1b;
  font-size: 14px;
  border-left: 2px solid #991b1b;
}
.switch {
  margin-top: var(--space-lg);
  text-align: center;
  font-size: 14px;
  color: var(--color-text-soft);
}
.switch a {
  border-bottom: 1px solid var(--color-text);
}
</style>
