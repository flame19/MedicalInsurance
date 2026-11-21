import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles.css';
import App from './App.vue';
import HomePage from './views/HomePage.vue';
import ContractsPage from './views/ContractsPage.vue';
import AboutPage from './views/AboutPage.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomePage, meta: { title: 'Головна' } },
    { path: '/contracts', name: 'contracts', component: ContractsPage, meta: { title: 'Договори' } },
    { path: '/about', name: 'about', component: AboutPage, meta: { title: 'Про проєкт' } },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
  scrollBehavior(to, from, savedPosition) {
    return savedPosition ?? { top: 0 };
  },
});

router.afterEach((to) => {
  document.title = to.meta.title + ' · Реєстр медичного страхування';
});

createApp(App).use(router).mount('#app');
