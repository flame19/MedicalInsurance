<script setup>
import { reactive, ref } from 'vue';
import { createEntry, explainError } from '../api.js';

const props = defineProps({
  clientsCount: { type: Number, required: true },
  plansCount: { type: Number, required: true },
});

const initiallyOpen = !props.clientsCount || !props.plansCount;
const emit = defineEmits(['created']);
const client = reactive({ fullName: '', email: '', phone: '' });
const plan = reactive({ name: '', coverageLimit: '', annualPremium: '' });
const pending = ref('');
const feedback = ref(null);

function save(resource, form, values, label) {
  pending.value = resource;
  feedback.value = null;
  createEntry(resource, values)
    .then((data) => {
      for (const key of Object.keys(form)) form[key] = '';
      feedback.value = { text: label, failed: false };
      emit('created', resource, data);
    })
    .catch((error) => {
      feedback.value = { text: explainError(error), failed: true };
    })
    .finally(() => { pending.value = ''; });
}

function saveClient() {
  if (!client.fullName.trim()) {
    feedback.value = { text: 'Введіть ПІБ клієнта.', failed: true };
    return;
  }
  save('/clients', client, {
    fullName: client.fullName.trim(),
    email: client.email.trim(),
    phone: client.phone.trim() || null,
  }, 'Клієнта додано.');
}

function savePlan() {
  if (!plan.name.trim()) {
    feedback.value = { text: 'Введіть назву страхової програми.', failed: true };
    return;
  }
  save('/insurance-plans', plan, {
    name: plan.name.trim(),
    coverageLimit: Number(plan.coverageLimit),
    annualPremium: Number(plan.annualPremium),
  }, 'Страхову програму додано.');
}
</script>

<template>
  <details class="card surface references mt-4" :open="initiallyOpen">
    <summary>Довідники клієнтів і програм</summary>
    <div class="card-body pt-0">
      <p class="muted">Додані клієнти та програми одразу доступні у формі договору.</p>
      <div v-if="feedback" class="alert" :class="feedback.failed ? 'alert-danger' : 'alert-success'" :role="feedback.failed ? 'alert' : 'status'">
        {{ feedback.text }}
      </div>
      <div class="row g-4">
        <form class="col-12 col-lg-6" @submit.prevent="saveClient">
          <h3 class="h6">Новий клієнт</h3>
          <fieldset :disabled="Boolean(pending)">
            <label for="client-name" class="form-label">ПІБ</label>
            <input id="client-name" v-model="client.fullName" class="form-control mb-2" required maxlength="160" />
            <label for="client-email" class="form-label">Електронна пошта</label>
            <input id="client-email" v-model="client.email" class="form-control mb-2" type="email" required maxlength="254" />
            <label for="client-phone" class="form-label">Телефон (необов’язково)</label>
            <input id="client-phone" v-model="client.phone" class="form-control" type="tel" maxlength="32" />
            <button class="btn btn-outline-primary mt-3" type="submit">Додати клієнта</button>
          </fieldset>
        </form>
        <form class="col-12 col-lg-6" @submit.prevent="savePlan">
          <h3 class="h6">Нова страхова програма</h3>
          <fieldset :disabled="Boolean(pending)">
            <label for="plan-name" class="form-label">Назва</label>
            <input id="plan-name" v-model="plan.name" class="form-control mb-2" required maxlength="120" />
            <label for="plan-limit" class="form-label">Ліміт покриття, грн</label>
            <input id="plan-limit" v-model="plan.coverageLimit" class="form-control mb-2" type="number" min="0.01" max="999999999999.99" step="0.01" required />
            <label for="plan-premium" class="form-label">Річна премія, грн</label>
            <input id="plan-premium" v-model="plan.annualPremium" class="form-control" type="number" min="0.01" max="999999999999.99" step="0.01" required />
            <button class="btn btn-outline-primary mt-3" type="submit">Додати програму</button>
          </fieldset>
        </form>
      </div>
    </div>
  </details>
</template>

