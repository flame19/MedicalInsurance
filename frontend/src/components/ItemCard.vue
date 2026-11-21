<script setup>
import { RouterLink } from 'vue-router';

defineProps({
  title: { type: String, required: true },
  description: { type: String, required: true },
  to: { type: String, required: true },
  label: { type: String, required: true },
});
</script>

<template>
  <article class="card surface h-100">
    <div class="card-body d-flex flex-column align-items-start">
      <h2 class="h5">{{ title }}</h2>
      <p class="muted mb-4">{{ description }}</p>
      <RouterLink :to="to" class="text-action mt-auto">{{ label }} <span aria-hidden="true">→</span></RouterLink>
    </div>
  </article>
</template>
