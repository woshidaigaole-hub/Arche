<template>
  <div class="trajectory-page">
    <h2 class="section-label">
      <img class="section-icon" src="/spinner-icon.png" alt="" width="18" height="18" />
      轨迹
    </h2>
    <p class="empty">记录思考的路径 —— 即将上线</p>
  </div>
</template>

<script setup>
</script>

<style scoped>
.trajectory-page {
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

.empty {
  color: var(--color-text-soft);
  font-size: 16px;
  text-align: center;
  padding: var(--space-xl) 0;
}
</style>
