<template>
  <div class="home">
    <!-- 精选区 -->
    <section v-if="topArticles.length > 0" class="featured">
      <h2 class="section-label">精选</h2>
      <article v-for="article in topArticles" :key="article.id" class="card featured-card">
        <router-link :to="`/article/${article.id}`">
          <h3 class="card-title">{{ article.title }}</h3>
          <p class="card-summary">{{ article.summary || article.content.slice(0, 180) + '...' }}</p>
          <div class="card-meta">
            <span>{{ article.authorName }}</span>
            <span>{{ formatDate(article.createTime) }}</span>
          </div>
        </router-link>
      </article>
    </section>

    <!-- 最近更新 —— 双列网格 -->
    <section class="normal">
      <h2 class="section-label">最近更新</h2>
      <div class="card-grid">
        <article v-for="article in normalArticles" :key="article.id" class="card">
          <router-link :to="`/article/${article.id}`">
            <h3 class="card-title">{{ article.title }}</h3>
            <p class="card-summary">{{ article.summary || article.content.slice(0, 100) + '...' }}</p>
            <div class="card-meta">
              <span>{{ article.authorName }}</span>
              <span>{{ formatDate(article.createTime) }}</span>
            </div>
          </router-link>
        </article>
      </div>
      <p v-if="normalArticles.length === 0 && topArticles.length === 0" class="empty">
        暂无文章。
      </p>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { articleApi } from '../api/index.js'

const articles = ref([])

const topArticles    = computed(() => articles.value.filter(a => a.isTop === 1))
const normalArticles = computed(() => articles.value.filter(a => a.isTop === 0))

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
.home {
  padding-top: var(--space-xl);
  padding-bottom: var(--space-xl);
}

.section-label {
  font-family: var(--font-sans);
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--color-border);
}

/* ===== 精选区 ===== */
.featured {
  margin-bottom: var(--space-xl);
}
.featured-card {
  padding: var(--space-lg);
  margin-bottom: var(--space-md);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  background: var(--color-surface);
  transition: border-color 0.15s ease;
}
.featured-card:hover {
  border-color: var(--color-text-soft);
}
.featured-card .card-title {
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.5px;
}
.featured-card .card-summary {
  -webkit-line-clamp: 3;
}

/* ===== 双列网格 ===== */
.card-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-md);
}

/* ===== 卡片通用 ===== */
.card {
  padding: var(--space-md) var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: 8px;
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
  grid-column: 1 / -1;
  color: var(--color-text-soft);
  font-size: 14px;
  text-align: center;
  padding: var(--space-xl) 0;
}

/* ===== 手机端 ===== */
@media (max-width: 768px) {
  .home {
    padding-top: var(--space-lg);
  }
  .card-grid {
    grid-template-columns: 1fr;
    gap: var(--space-sm);
  }
  .featured-card {
    padding: var(--space-md);
  }
  .featured-card .card-title {
    font-size: 20px;
  }
  .card {
    padding: var(--space-md);
  }
  .card-title {
    font-size: 16px;
  }
  .card-summary {
    font-size: 13px;
  }
}
</style>
