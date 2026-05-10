<template>
  <div class="creation-page">
    <h2 class="section-label">
      <img class="section-icon" src="/bulb-icon.png" alt="" width="18" height="18" />
      创作
    </h2>

    <!-- 上传按钮（登录后才显示） -->
    <div v-if="isLoggedIn" class="upload-toggle">
      <button class="btn" @click="showUpload = !showUpload">
        {{ showUpload ? '取消' : '+ 上传画作' }}
      </button>
    </div>

    <!-- 上传表单 -->
    <form v-if="showUpload" class="upload-form" @submit.prevent="handleUpload">
      <div class="form-group">
        <label>标题</label>
        <input v-model="form.title" type="text" required placeholder="画作名称" />
      </div>
      <div class="form-group">
        <label>创作日期</label>
        <input v-model="form.createDate" type="date" required />
      </div>
      <div class="form-group">
        <label>图片</label>
        <input type="file" accept="image/*" required @change="onFileChange" />
      </div>
      <button type="submit" class="btn btn-primary" :disabled="uploading">
        {{ uploading ? '上传中...' : '上传' }}
      </button>
    </form>

    <!-- 画廊：按时间倒序 -->
    <div class="gallery">
      <div v-for="artwork in artworks" :key="artwork.id" class="artwork-card">
        <img :src="artwork.imagePath" :alt="artwork.title" class="artwork-image" />
        <div class="artwork-info">
          <h3>{{ artwork.title }}</h3>
          <span>{{ artwork.createDate }}</span>
        </div>
      </div>
      <p v-if="artworks.length === 0" class="empty">暂无画作。</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/index.js'

const showUpload = ref(false)
const uploading = ref(false)
const artworks = ref([])
const isLoggedIn = ref(!!localStorage.getItem('token'))
const file = ref(null)

const form = ref({ title: '', createDate: '' })

const onFileChange = (e) => {
  file.value = e.target.files[0]
}

const handleUpload = async () => {
  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', file.value)
    formData.append('title', form.value.title)
    formData.append('createDate', form.value.createDate)

    await api.post('/artworks', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    showUpload.value = false
    form.value = { title: '', createDate: '' }
    file.value = null
    await fetchArtworks()
  } catch (e) {
    console.error('上传失败', e)
    alert('上传失败')
  } finally {
    uploading.value = false
  }
}

const fetchArtworks = async () => {
  try {
    const res = await api.get('/artworks')
    artworks.value = res.data.data
  } catch (e) {
    console.error('加载画作失败', e)
  }
}

onMounted(fetchArtworks)
</script>

<style scoped>
.creation-page {
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

.upload-toggle {
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
.btn-primary {
  background: var(--color-text);
  color: var(--color-surface);
  border-color: var(--color-text);
}
.btn-primary:hover {
  opacity: 0.85;
}

.upload-form {
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: var(--color-surface);
  margin-bottom: var(--space-xl);
}
.form-group {
  margin-bottom: var(--space-md);
}
.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 400;
  margin-bottom: var(--space-xs);
  color: var(--color-text-soft);
}
.form-group input[type="text"],
.form-group input[type="date"] {
  width: 100%;
  max-width: 400px;
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  font-size: 16px;
  background: #fff;
}
.form-group input[type="file"] {
  font-size: 16px;
}

.gallery {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-lg);
}
.artwork-card {
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: var(--color-surface);
  overflow: hidden;
  transition: border-color 0.15s ease;
}
.artwork-card:hover {
  border-color: var(--color-text-soft);
}
.artwork-image {
  width: 100%;
  aspect-ratio: 4 / 3;
  object-fit: cover;
}
.artwork-info {
  padding: var(--space-md);
}
.artwork-info h3 {
  font-size: 18px;
  font-weight: 400;
  line-height: 1.4;
  margin-bottom: var(--space-xs);
  letter-spacing: -0.3px;
}
.artwork-info span {
  font-size: 13px;
  color: var(--color-text-soft);
}

.empty {
  grid-column: 1 / -1;
  color: var(--color-text-soft);
  font-size: 16px;
  text-align: center;
  padding: var(--space-xl) 0;
}

@media (max-width: 768px) {
  .creation-page {
    padding-top: var(--space-lg);
  }
  .gallery {
    grid-template-columns: 1fr;
    gap: var(--space-md);
  }
  .artwork-info h3 {
    font-size: 16px;
  }
  .artwork-info {
    padding: var(--space-md);
  }
}
</style>
