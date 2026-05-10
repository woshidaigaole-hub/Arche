<template>
  <div class="app-layout">
    <NavBar />
    <main class="main-content">
      <div class="container">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
</script>

<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}
.main-content {
  margin-left: var(--sidebar-width);
  flex: 1;
  min-width: 0;
}

/* 手机端：侧边栏变成顶部条，内容区无需左边距 */
@media (max-width: 768px) {
  .main-content {
    margin-left: 0;
    margin-top: 52px;
  }
}
</style>
