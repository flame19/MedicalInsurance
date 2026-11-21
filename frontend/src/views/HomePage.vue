<script setup>
import { RouterLink } from 'vue-router';
import ItemCard from '../components/ItemCard.vue';
</script>

<template>
  <div class="container shell page">
    <section class="hero">
      <div class="hero-content">
        <h1>Реєстр договорів<br class="d-none d-md-inline" /> медичного страхування</h1>
        <p>Ведіть договори, перевіряйте строки дії та пов’язуйте записи з клієнтами і страховими програмами.</p>
        <RouterLink to="/contracts" class="btn btn-light">Відкрити договори <span aria-hidden="true">→</span></RouterLink>
      </div>
    </section>
    <div class="row g-3 g-md-4 mt-2">
      <div class="col-12 col-md-4">
        <ItemCard title="Переглянути договори" description="Знайдіть договір за номером або клієнтом і перевірте його статус."
          to="/contracts" label="Перейти до реєстру" />
      </div>
      <div class="col-12 col-md-4">
        <ItemCard title="Додати договір" description="Оберіть клієнта й програму, вкажіть номер, дати та статус договору."
          to="/contracts?new=1" label="Відкрити форму" />
      </div>
      <div class="col-12 col-md-4">
        <ItemCard title="Про систему" description="Перегляньте, які дані зберігає реєстр і як клієнт працює із сервером."
          to="/about" label="Детальніше" />
      </div>
    </div>
  </div>
</template>
