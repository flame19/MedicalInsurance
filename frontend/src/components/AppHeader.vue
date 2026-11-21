<script setup>
import { RouterLink } from 'vue-router';
</script>

<template>
  <header class="app-header">
    <div class="container shell header-layout">
      <RouterLink class="brand" to="/">
        <span class="brand-mark" aria-hidden="true">+</span>
        <span>Медичне страхування</span>
      </RouterLink>
      <nav class="app-nav" aria-label="Основна навігація">
        <RouterLink to="/">Головна</RouterLink>
        <RouterLink to="/contracts">Договори</RouterLink>
        <RouterLink to="/about">Про проєкт</RouterLink>
      </nav>
    </div>
  </header>
</template>
