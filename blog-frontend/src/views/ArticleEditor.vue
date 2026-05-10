<template>
  <div class="editor">
    <header class="editor-header">
      <h1 class="page-title">{{ isEdit ? '编辑文章' : '写文章' }}</h1>
      <div class="editor-actions">
        <router-link to="/" class="btn">取消</router-link>
        <button class="btn" @click="save" :disabled="saving">
          {{ saving ? '保存中...' : '发布' }}
        </button>
      </div>
    </header>

    <div v-if="error" class="error">{{ error }}</div>

    <input v-model="form.title" class="input" type="text"
           placeholder="文章标题" style="font-family:var(--font-sans);font-size:24px;margin-bottom:var(--space-md)" />

    <input v-model="form.summary" class="input" type="text"
           placeholder="摘要（选填）" style="margin-bottom:var(--space-lg)" />

    <textarea v-model="form.content" class="editor-textarea"
              placeholder="正文（支持 Markdown）..."></textarea>
  </div>
</template>

<script setup>
/**
 * 文章编辑器 —— 共用新建和编辑两种模式
 * 通过 URL 有无 id 参数区分：/editor 新建，/editor/3 编辑第3篇
 */
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { articleApi } from '../api/index.js'

const route = useRoute()
const router = useRouter()
const isEdit = !!route.params.id  // !! = 转布尔值
const saving = ref(false)
const error = ref('')
const form = reactive({ title: '', summary: '', content: '' })

// 编辑模式：加载已有文章内容
onMounted(async () => {
  if (isEdit) {
    try {
      const res = await articleApi.detail(route.params.id)
      const a = res.data.data
      form.title = a.title
      form.summary = a.summary
      form.content = a.content
    } catch (e) {
      error.value = '加载文章失败'
    }
  }
})

const save = async () => {
  if (!form.title.trim() || !form.content.trim()) {
    error.value = '标题和正文不能为空'
    return
  }
  saving.value = true
  error.value = ''
  try {
    if (isEdit) {
      await articleApi.update(route.params.id, form)
    } else {
      await articleApi.create(form)
    }
    router.push('/')  // 保存成功回首页
  } catch (e) {
    error.value = e.response?.data?.message || '保存失败'
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.editor {
  padding-top: var(--space-lg);
  padding-bottom: var(--space-xl);
}
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-lg);
}
.editor-actions {
  display: flex;
  gap: var(--space-sm);
}
.editor-textarea {
  width: 100%;
  min-height: 400px;
  padding: 0;
  font-family: var(--font-sans);
  font-size: 17px;
  line-height: 1.9;
  border: none;
  resize: vertical;
  outline: none;
  background: transparent;
}
.error {
  margin-bottom: var(--space-md);
  padding: var(--space-sm) var(--space-md);
  background: #fef2f2;
  color: #991b1b;
  font-size: 14px;
  border-left: 2px solid #991b1b;
}

/* ===== 手机端 ===== */
@media (max-width: 768px) {
  .editor {
    padding-top: var(--space-md);
  }
  .editor-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-sm);
  }
  .editor-textarea {
    min-height: 300px;
    font-size: 16px;
  }
}
</style>
