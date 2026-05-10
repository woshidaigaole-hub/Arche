import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/',           name: 'Home',           component: () => import('../views/Home.vue') },
  { path: '/explore',    name: 'Explore',        component: () => import('../views/Explore.vue') },
  { path: '/archive',    name: 'Archive',        component: () => import('../views/Archive.vue') },
  { path: '/about',      name: 'About',          component: () => import('../views/About.vue') },
  { path: '/login',      name: 'Login',          component: () => import('../views/Login.vue') },
  { path: '/register',   name: 'Register',       component: () => import('../views/Register.vue') },
  { path: '/article/:id',name: 'ArticleDetail',  component: () => import('../views/ArticleDetail.vue') },
  { path: '/editor',     name: 'Editor',         component: () => import('../views/ArticleEditor.vue') },
  { path: '/editor/:id', name: 'EditorEdit',     component: () => import('../views/ArticleEditor.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
