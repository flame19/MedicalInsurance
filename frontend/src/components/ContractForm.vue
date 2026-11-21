<script setup>
import { computed, nextTick, reactive, ref, watch } from 'vue';
import { contractStatuses } from '../api.js';

const props = defineProps({
  contract: { type: Object, default: null },
  clients: { type: Array, required: true },
  plans: { type: Array, required: true },
  pending: Boolean,
  error: { type: String, default: '' },
});

const emit = defineEmits(['save', 'close']);
const form = reactive({});
const heading = ref(null);
const validation = ref('');
const ready = computed(() => props.clients.length > 0 && props.plans.length > 0);

watch(() => props.contract, (contract) => {
  Object.assign(form, contract ?? {
    contractNumber: '',
    startDate: '',
    endDate: '',
    status: 'DRAFT',
    clientId: '',
    insurancePlanId: '',
  });
  validation.value = '';
  nextTick(() => heading.value?.focus());
}, { immediate: true });

function submit() {
  const contractNumber = form.contractNumber.trim();
  if (!contractNumber) {
    validation.value = 'Введіть номер договору.';
    return;
  }
  if (form.endDate < form.startDate) {
    validation.value = 'Дата закінчення має бути не раніше дати початку.';
    return;
  }
  validation.value = '';
  emit('save', {
    contractNumber,
    startDate: form.startDate,
    endDate: form.endDate,
    status: form.status,
    clientId: form.clientId,
    insurancePlanId: form.insurancePlanId,
  });
}
</script>

<template>
  <section class="card surface mb-4" aria-labelledby="contract-form-title">
    <div class="card-body">
      <h2 id="contract-form-title" ref="heading" class="h4 mb-3" tabindex="-1">
        {{ contract ? 'Редагувати договір' : 'Новий договір' }}
      </h2>
      <div v-if="!ready" class="alert alert-info">Спочатку додайте клієнта й страхову програму в довідниках нижче.</div>
      <div v-if="error || validation" class="alert alert-danger" role="alert">{{ validation || error }}</div>
      <form @submit.prevent="submit">
        <fieldset :disabled="pending || !ready" class="row g-3">
          <div class="col-12 col-md-8">
            <label for="contract-number" class="form-label">Номер договору</label>
            <input id="contract-number" v-model="form.contractNumber" class="form-control" type="text" required maxlength="40" />
          </div>
          <div class="col-12 col-md-4">
            <label for="contract-status" class="form-label">Статус</label>
            <select id="contract-status" v-model="form.status" class="form-select" required>
              <option v-for="(label, status) in contractStatuses" :key="status" :value="status">{{ label }}</option>
            </select>
          </div>
          <div class="col-12 col-md-6">
            <label for="contract-client" class="form-label">Клієнт</label>
            <select id="contract-client" v-model="form.clientId" class="form-select" required>
              <option disabled value="">Оберіть клієнта</option>
              <option v-for="client in clients" :key="client.id" :value="client.id">{{ client.fullName }} · №{{ client.id }}</option>
            </select>
          </div>
          <div class="col-12 col-md-6">
            <label for="contract-plan" class="form-label">Страхова програма</label>
            <select id="contract-plan" v-model="form.insurancePlanId" class="form-select" required>
              <option disabled value="">Оберіть програму</option>
              <option v-for="plan in plans" :key="plan.id" :value="plan.id">{{ plan.name }} · №{{ plan.id }}</option>
            </select>
          </div>
          <div class="col-12 col-md-6">
            <label for="contract-start" class="form-label">Дата початку</label>
            <input id="contract-start" v-model="form.startDate" class="form-control" type="date" required />
          </div>
          <div class="col-12 col-md-6">
            <label for="contract-end" class="form-label">Дата закінчення</label>
            <input id="contract-end" v-model="form.endDate" class="form-control" type="date" :min="form.startDate || undefined" required />
          </div>
        </fieldset>
        <div class="d-flex flex-wrap gap-2 mt-4">
          <button class="btn btn-primary" type="submit" :disabled="pending || !ready">
            {{ pending ? 'Збереження…' : contract ? 'Зберегти зміни' : 'Створити договір' }}
          </button>
          <button class="btn btn-outline-secondary" type="button" :disabled="pending" @click="emit('close')">Скасувати</button>
        </div>
      </form>
    </div>
  </section>
</template>
