<script setup>
import { RouterLink } from 'vue-router';
</script>

<template>
  <div class="container shell page">
    <div class="page-heading">
      <h1>Про проєкт</h1>
      <p class="muted">Навчальний застосунок для обліку договорів медичного страхування.</p>
    </div>
    <div class="row g-4">
      <div class="col-12 col-lg-7">
        <section class="card surface h-100">
          <div class="card-body">
            <h2 class="h4">Модель даних</h2>
            <dl class="about-list mb-0">
              <dt>Договір</dt>
              <dd>Номер, строк дії, статус, клієнт і страхова програма.</dd>
              <dt>Клієнт</dt>
              <dd>ПІБ, електронна пошта та телефон страхувальника.</dd>
              <dt>Страхова програма</dt>
              <dd>Назва, ліміт покриття та річна страхова премія.</dd>
            </dl>
          </div>
        </section>
      </div>
      <div class="col-12 col-lg-5">
        <section class="card surface h-100">
          <div class="card-body d-flex flex-column align-items-start">
            <h2 class="h4">Як працює застосунок</h2>
            <p class="muted">Vue 3 відображає дані й керує навігацією. Axios передає запити до REST API Spring Boot. Сервер зберігає записи в PostgreSQL.</p>
            <p class="muted">Договори можна переглядати, додавати, редагувати й видаляти зі сторінки реєстру.</p>
            <RouterLink to="/contracts" class="text-action mt-auto">Відкрити реєстр <span aria-hidden="true">→</span></RouterLink>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>
