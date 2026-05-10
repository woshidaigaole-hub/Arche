<template>
  <!--
    桌面端：左侧固定全高侧边栏
    手机端：顶部固定条 + 汉堡菜单展开
  -->
  <aside class="sidebar" :class="{ 'mobile-open': menuOpen }">
    <!-- 手机端顶部条 -->
    <div class="mobile-bar" @click="menuOpen = !menuOpen">
      <router-link to="/" class="sidebar-logo" @click.stop>Arche</router-link>
      <button class="hamburger" @click.stop="menuOpen = !menuOpen">
        <span></span><span></span><span></span>
      </button>
    </div>

    <!-- 导航主体（手机端默认隐藏，点汉堡展开） -->
    <div class="sidebar-body" @click="menuOpen = false">
      <nav class="sidebar-nav">
        <router-link to="/" class="nav-item">Arche</router-link>
        <router-link to="/creation" class="nav-item">创作</router-link>
        <router-link to="/trajectory" class="nav-item">轨迹</router-link>
      </nav>

      <div class="sidebar-footer">
        <template v-if="isLoggedIn">
          <a href="#" class="nav-item" @click.prevent="logout">退出</a>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-item">登录</router-link>
        </template>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const menuOpen = ref(false)
const isLoggedIn = ref(!!localStorage.getItem('token'))

const logout = () => {
  localStorage.removeItem('token')
  isLoggedIn.value = false
  window.location.href = '/'
}
</script>

<style scoped>
/* ===== 桌面端：固定侧边栏 ===== */
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
  width: var(--sidebar-width);
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: var(--space-lg) var(--space-md);
  border-right: 1px solid var(--color-border);
  background: #f7f7f3;
}

/* 手机端顶部条：桌面端隐藏 */
.mobile-bar {
  display: none;
}

.sidebar-logo {
  font-family: var(--font-sans);
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 1px;
  padding: var(--space-sm) var(--space-sm);
  margin-bottom: var(--space-xl);
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  flex: 1;
}

.sidebar-body {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.sidebar-footer {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  padding-top: var(--space-md);
  border-top: 1px solid var(--color-border);
}

.nav-item {
  display: block;
  padding: var(--space-sm) var(--space-sm);
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text-soft);
  border-radius: 6px;
  transition: all 0.15s ease;
  letter-spacing: 0.3px;
}
.nav-item:hover {
  color: var(--color-text);
  background: rgba(0, 0, 0, 0.04);
}
.nav-item.active,
.nav-item.router-link-exact-active {
  color: var(--color-text);
  background: rgba(0, 0, 0, 0.06);
}

/* 汉堡按钮 */
.hamburger {
  display: flex;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}
.hamburger span {
  display: block;
  width: 22px;
  height: 2px;
  background: var(--color-text);
  border-radius: 1px;
  transition: all 0.2s ease;
}

/* ===== 手机端：< 768px ===== */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: auto;
    padding: 0;
    border-right: none;
    border-bottom: 1px solid var(--color-border);
    flex-direction: column;
  }

  /* 顶部条：Logo 左 + 汉堡右 */
  .mobile-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 52px;
    padding: 0 var(--space-md);
  }
  .mobile-bar .sidebar-logo {
    margin-bottom: 0;
    font-size: 18px;
    padding: 0;
  }

  /* 菜单主体默认隐藏 */
  .sidebar-body {
    display: none;
    padding: 0 var(--space-md) var(--space-md);
    background: #f7f7f3;
    border-bottom: 1px solid var(--color-border);
  }
  /* 汉堡激活时展开 */
  .mobile-open .sidebar-body {
    display: block;
  }

  .sidebar-nav {
    gap: 2px;
  }
  .sidebar-footer {
    margin-top: var(--space-sm);
  }
}
</style>
