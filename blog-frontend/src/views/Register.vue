<template>
  <div class="register">
    <div class="form-card">
      <h1 class="page-title">注册</h1>
      <p class="page-subtitle">注册后需管理员审核通过才能发布文章</p>

      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>

      <form v-if="!success" @submit.prevent="handleRegister">
        <label class="label">用户名</label>
        <input v-model="form.username" class="input" type="text" required />

        <label class="label">昵称（选填）</label>
        <input v-model="form.nickname" class="input" type="text" />

        <label class="label">密码</label>
        <input v-model="form.password" class="input" type="password" required />

        <button class="btn" type="submit" :disabled="loading" style="margin-top:24px;width:100%">
          {{ loading ? '提交中...' : '注册' }}
        </button>
      </form>

      <p class="switch">
        已有账号？<router-link to="/login">登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { authApi } from '../api/index.js'

const form = reactive({ username: '', password: '', nickname: '' })
const loading = ref(false)
const error = ref('')
const success = ref('')

const handleRegister = async () => {
  error.value = ''
  loading.value = true
  try {
    await authApi.register(form)
    success.value = '注册成功！请等待管理员审核。'
  } catch (e) {
    error.value = e.response?.data?.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register {
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
.success {
  margin-top: var(--space-md);
  padding: var(--space-sm) var(--space-md);
  background: #f0fdf4;
  color: #166534;
  font-size: 14px;
  border-left: 2px solid #166534;
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
