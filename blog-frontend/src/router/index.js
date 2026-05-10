import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/',           name: 'Home',           component: () => import('../views/Articles.vue') },
  { path: '/editor',     name: 'Editor',         component: () => import('../views/ArticleEditor.vue') },
  { path: '/editor/:id', name: 'EditorEdit',     component: () => import('../views/ArticleEditor.vue') },
  { path: '/creation',   name: 'Creation',      component: () => import('../views/Creation.vue') },
  { path: '/trajectory', name: 'Trajectory',     component: () => import('../views/Trajectory.vue') },
  { path: '/login',      name: 'Login',          component: () => import('../views/Login.vue') },
  { path: '/register',   name: 'Register',       component: () => import('../views/Register.vue') },
  { path: '/article/:id',name: 'ArticleDetail',  component: () => import('../views/ArticleDetail.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
