<template>
  <!-- 文章详情页 —— 纯排版，克制设计 -->
  <div v-if="article" class="article-detail">
    <header class="article-header">
      <h1 class="article-title">{{ article.title }}</h1>
      <div class="article-meta">
        <span>{{ article.authorName }}</span>
        <span>{{ formatDate(article.createTime) }}</span>
        <span v-if="article.updateTime !== article.createTime">
          更新于 {{ formatDate(article.updateTime) }}
        </span>
      </div>
    </header>

    <!-- Markdown 渲染区 -->
    <article class="article-content" v-html="article.renderedContent"></article>

    <div class="article-footer">
      <router-link to="/" class="btn">← 返回</router-link>
    </div>
  </div>

  <div v-else-if="loading" class="empty">加载中...</div>
  <div v-else class="empty">文章不存在</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { articleApi } from '../api/index.js'
import { marked } from 'marked'

const route = useRoute()  // 获取当前 URL 参数，比如 /article/3 里的 3
const article = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await articleApi.detail(route.params.id)  // route.params.id = URL里的{id}
    article.value = res.data.data
    if (article.value) {
      // marked.parse() = 把 Markdown 字符串转成 HTML
      article.value.renderedContent = marked.parse(article.value.content)
    }
  } catch (e) {
    console.error('加载文章失败', e)
  } finally {
    loading.value = false
  }
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric', month: 'long', day: 'numeric'
  })
}
</script>

<style scoped>
.article-detail {
  padding-top: var(--space-xl);
  padding-bottom: var(--space-xl);
}
.article-header {
  margin-bottom: var(--space-xl);
  padding-bottom: var(--space-lg);
  border-bottom: 1px solid var(--color-border);
}
.article-title {
  font-family: var(--font-sans);
  font-size: 32px;
  font-weight: 700;
  line-height: 1.3;
  letter-spacing: -0.5px;
}
.article-meta {
  display: flex;
  gap: var(--space-md);
  margin-top: var(--space-md);
  font-size: 14px;
  color: var(--color-text-soft);
}

/* Markdown 渲染后的排版 —— 克制、舒适 */
.article-content {
  font-family: var(--font-sans);
  font-size: 17px;
  line-height: 1.9;
  word-break: break-word;
}
/* >>> = 穿透 scoped 样式，影响 v-html 渲染的 HTML */
.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3) {
  font-family: var(--font-sans);
  font-weight: 600;
  margin: var(--space-xl) 0 var(--space-md);
  line-height: 1.3;
}
.article-content :deep(h2) { font-size: 24px; }
.article-content :deep(h3) { font-size: 20px; }
.article-content :deep(p) {
  margin-bottom: var(--space-md);
}
.article-content :deep(blockquote) {
  margin: var(--space-md) 0;
  padding: var(--space-sm) var(--space-md);
  border-left: 2px solid var(--color-text);
  color: var(--color-text-soft);
  font-style: italic;
}
.article-content :deep(code) {
  background: #f5f5f4;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 0.9em;
  font-family: 'SF Mono', 'Fira Code', monospace;
}
.article-content :deep(pre) {
  background: #f5f5f4;
  padding: var(--space-md);
  margin: var(--space-md) 0;
  border-radius: 4px;
  overflow-x: auto;
}
.article-content :deep(pre code) {
  background: none;
  padding: 0;
}
.article-content :deep(img) {
  max-width: 100%;
  margin: var(--space-md) 0;
}

.article-footer {
  margin-top: var(--space-xl);
  padding-top: var(--space-lg);
  border-top: 1px solid var(--color-border);
}
.empty {
  text-align: center;
  color: var(--color-text-soft);
  padding: var(--space-xl) 0;
}

/* ===== 手机端 ===== */
@media (max-width: 768px) {
  .article-detail {
    padding-top: var(--space-lg);
  }
  .article-title {
    font-size: 24px;
  }
  .article-content {
    font-size: 16px;
  }
  .article-content :deep(h2) { font-size: 20px; }
  .article-content :deep(h3) { font-size: 18px; }
}
</style>
