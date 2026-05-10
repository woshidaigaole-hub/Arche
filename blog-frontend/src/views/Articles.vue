<template>
  <div class="articles-page">
    <h2 class="section-label">文章</h2>
    <div class="article-list">
      <article v-for="article in articles" :key="article.id" class="card">
        <router-link :to="`/article/${article.id}`">
          <h3 class="card-title">{{ article.title }}</h3>
          <p class="card-summary">{{ article.summary || article.content.slice(0, 150) + '...' }}</p>
          <div class="card-meta">
            <span>{{ article.authorName }}</span>
            <span>{{ formatDate(article.createTime) }}</span>
          </div>
        </router-link>
      </article>
      <p v-if="articles.length === 0" class="empty">暂无文章。</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi } from '../api/index.js'

const articles = ref([])

onMounted(async () => {
  try {
    const res = await articleApi.list()
    articles.value = res.data.data
  } catch (e) {
    console.error('加载文章失败', e)
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
.articles-page {
  padding-top: var(--space-xl);
  padding-bottom: var(--space-xl);
}

.section-label {
  font-family: var(--font-sans);
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-soft);
  letter-spacing: 3px;
  text-transform: uppercase;
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--color-border);
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.card {
  padding: var(--space-md) var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: var(--color-surface);
  transition: border-color 0.15s ease;
}
.card:hover {
  border-color: var(--color-text-soft);
}

.card-title {
  font-family: var(--font-sans);
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: var(--space-xs);
  letter-spacing: -0.3px;
}

.card-summary {
  color: var(--color-text-soft);
  font-size: 14px;
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

.empty {
  color: var(--color-text-soft);
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
