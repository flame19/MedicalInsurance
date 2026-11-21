<script setup>
import { RouterView } from 'vue-router';
import AppHeader from './components/AppHeader.vue';
import AppFooter from './components/AppFooter.vue';
</script>

<template>
  <a class="skip-link" href="#main">Перейти до вмісту</a>
  <AppHeader />
  <main id="main" class="flex-grow-1" tabindex="-1">
    <RouterView />
  </main>
  <AppFooter />
</template>
