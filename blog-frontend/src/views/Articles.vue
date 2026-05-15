<template>
  <div class="articles-page">
    <h2 class="section-label">
      <img class="section-icon" src="/book-icon.png" alt="" width="18" height="18" />
      文章
    </h2>

    <div v-if="isLoggedIn" class="action-bar">
      <router-link to="/editor" class="btn">+ 写文章</router-link>
    </div>

    <div class="article-list">
      <article v-for="article in articles" :key="article.id" class="card">
        <router-link :to="`/article/${article.id}`" class="card-link">
          <h3 class="card-title">{{ article.title }}</h3>
          <p class="card-summary">{{ article.summary || article.content.slice(0, 150) + '...' }}</p>
          <div class="card-meta">
            <span>{{ article.authorName }}</span>
            <span>{{ formatDate(article.createTime) }}</span>
          </div>
        </router-link>
        <button
          v-if="canDelete(article)"
          class="delete-btn"
          @click="handleDelete(article)"
        >删除</button>
      </article>
      <p v-if="articles.length === 0" class="empty">暂无文章。</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi } from '../api/index.js'

const isLoggedIn = ref(!!localStorage.getItem('token'))
const articles = ref([])

const parseToken = () => {
  const token = localStorage.getItem('token')
  if (!token) return null
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload))
  } catch { return null }
}
const tokenPayload = parseToken()

const canDelete = (article) => {
  if (!tokenPayload) return false
  return article.authorId === tokenPayload.userId || tokenPayload.role === 'ADMIN'
}

const fetchArticles = async () => {
  try {
    const res = await articleApi.list()
    articles.value = res.data.data
  } catch (e) {
    console.error('加载文章失败', e)
  }
}

onMounted(fetchArticles)

const handleDelete = async (article) => {
  if (!confirm(`确定删除《${article.title}》？`)) return
  try {
    await articleApi.remove(article.id)
    articles.value = articles.value.filter(a => a.id !== article.id)
  } catch (e) {
    alert(e.response?.data?.message || '删除失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric', month: 'long', day: 'numeric'
  })
}
</script>

<style scoped>
.articles-page {
  padding-top: var(--space-xl);
  padding-bottom: var(--space-xl);
}

.section-label {
  font-family: var(--font-sans);
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 1px;
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  gap: 6px;
}
.section-icon {
  flex-shrink: 0;
}

.action-bar {
  margin-bottom: var(--space-md);
}

.btn {
  font-size: 13px;
  font-weight: 500;
  padding: 6px 16px;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  background: var(--color-surface);
  color: var(--color-text);
  cursor: pointer;
  transition: all 0.15s ease;
}
.btn:hover {
  border-color: var(--color-text-soft);
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.card {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: var(--space-md) var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  background: var(--color-surface);
  transition: border-color 0.15s ease;
}
.card:hover {
  border-color: var(--color-text-soft);
}

.card-link {
  flex: 1;
  min-width: 0;
}

.card-title {
  font-family: var(--font-sans);
  font-size: 18px;
  font-weight: 400;
  line-height: 1.4;
  margin-bottom: var(--space-xs);
  letter-spacing: -0.3px;
}

.card-summary {
  color: var(--color-text-soft);
  font-size: 16px;
  line-height: 1.7;
  margin-bottom: var(--space-sm);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-meta {
  display: flex;
  gap: var(--space-md);
  font-size: 13px;
  color: var(--color-text-soft);
}

.delete-btn {
  flex-shrink: 0;
  margin-left: var(--space-md);
  font-size: 13px;
  font-weight: 500;
  padding: 4px 12px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: transparent;
  color: var(--color-text-soft);
  cursor: pointer;
  transition: all 0.15s ease;
}
.delete-btn:hover {
  color: #d44;
  border-color: #d44;
}

.empty {
  color: var(--color-text-soft);
  font-size: 16px;
  text-align: center;
  padding: var(--space-xl) 0;
}

@media (max-width: 768px) {
  .articles-page {
    padding-top: var(--space-lg);
  }
  .card {
    padding: var(--space-md);
  }
  .card-title {
    font-size: 16px;
  }
}
</style>
