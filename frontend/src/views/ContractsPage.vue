<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ContractForm from '../components/ContractForm.vue';
import ReferenceSetup from '../components/ReferenceSetup.vue';
import { contractStatuses, createEntry, deleteContract, explainError, fetchList, updateContract } from '../api.js';

const route = useRoute();
const router = useRouter();
const abort = new AbortController();
const contracts = ref([]);
const clients = ref([]);
const plans = ref([]);
const loading = ref(true);
const loadError = ref('');
const formOpen = ref(false);
const editing = ref(null);
const deleting = ref(null);
const pending = ref(false);
const mutationError = ref('');
const notice = ref('');
const query = ref('');

const clientNames = computed(() => new Map(clients.value.map((client) => [client.id, client.fullName])));
const planNames = computed(() => new Map(plans.value.map((plan) => [plan.id, plan.name])));
const filtered = computed(() => {
  const term = query.value.trim().toLocaleLowerCase('uk');
  return contracts.value.filter((contract) =>
    [contract.contractNumber, clientNames.value.get(contract.clientId), planNames.value.get(contract.insurancePlanId)]
      .some((value) => value?.toLocaleLowerCase('uk').includes(term)));
});

const dateFormat = new Intl.DateTimeFormat('uk-UA', { timeZone: 'UTC' });
function dateText(value) {
  return dateFormat.format(new Date(value));
}

function load() {
  loading.value = true;
  loadError.value = '';
  const signal = abort.signal;
  return Promise.all([
    fetchList('/insurance-contracts', signal),
    fetchList('/clients', signal),
    fetchList('/insurance-plans', signal),
  ])
    .then(([items, people, programs]) => {
      contracts.value = items;
      clients.value = people;
      plans.value = programs;
    })
    .catch((error) => {
      if (!signal.aborted) loadError.value = explainError(error);
    })
    .finally(() => { loading.value = false; });
}

function openForm(contract = null) {
  editing.value = contract;
  deleting.value = null;
  formOpen.value = true;
  notice.value = '';
  mutationError.value = '';
}

function closeForm() {
  formOpen.value = false;
  editing.value = null;
  mutationError.value = '';
  if (route.query.new) router.replace({ name: 'contracts' });
}

function saveContract(payload) {
  const original = editing.value;
  const request = original
    ? updateContract(original.id, payload)
    : createEntry('/insurance-contracts', payload);
  pending.value = true;
  mutationError.value = '';
  request
    .then((saved) => {
      if (original) {
        contracts.value = contracts.value.map((item) => item.id === saved.id ? saved : item);
      } else {
        contracts.value.push(saved);
      }
      query.value = '';
      closeForm();
      notice.value = original
        ? 'Зміни договору «' + saved.contractNumber + '» збережено.'
        : 'Договір «' + saved.contractNumber + '» додано.';
    })
    .catch((error) => { mutationError.value = explainError(error); })
    .finally(() => { pending.value = false; });
}

function askDelete(contract) {
  closeForm();
  notice.value = '';
  deleting.value = contract;
}

function confirmDelete() {
  const target = deleting.value;
  pending.value = true;
  mutationError.value = '';
  deleteContract(target.id)
    .then(() => {
      contracts.value = contracts.value.filter((item) => item.id !== target.id);
      deleting.value = null;
      notice.value = 'Договір «' + target.contractNumber + '» видалено.';
    })
    .catch((error) => { mutationError.value = explainError(error); })
    .finally(() => { pending.value = false; });
}

function addReference(resource, item) {
  if (resource === '/clients') clients.value.push(item);
  else plans.value.push(item);
}

watch(() => route.query.new, (value) => {
  if (value === '1') openForm();
}, { immediate: true });
onMounted(load);
onUnmounted(() => abort.abort());
</script>

<template>
  <div class="container shell page">
    <div class="page-heading d-flex flex-wrap justify-content-between align-items-center gap-3">
      <div>
        <h1>Страхові договори</h1>
        <p class="muted">Переглядайте, додавайте й оновлюйте записи реєстру.</p>
      </div>
      <button class="btn btn-primary" type="button" :disabled="pending || loading || Boolean(loadError)" @click="openForm()">
        <span aria-hidden="true">+</span> Новий договір
      </button>
    </div>

    <div v-if="notice" class="alert alert-success" role="status">{{ notice }}</div>
    <div v-if="loadError" class="alert alert-danger d-flex flex-wrap justify-content-between align-items-center gap-3" role="alert">
      <span>{{ loadError }}</span>
      <button class="btn btn-sm btn-outline-danger" type="button" @click="load">Повторити</button>
    </div>

    <ContractForm v-if="formOpen && !loading && !loadError" :key="editing?.id ?? 'new'" :contract="editing" :clients="clients" :plans="plans"
      :pending="pending" :error="mutationError" @close="closeForm" @save="saveContract" />

    <section v-if="deleting" class="alert alert-warning mb-4" aria-labelledby="delete-title">
      <h2 id="delete-title" class="h5">Видалити договір «{{ deleting.contractNumber }}»?</h2>
      <p>Видалений запис зникне з реєстру.</p>
      <p v-if="mutationError" class="fw-semibold" role="alert">{{ mutationError }}</p>
      <div class="d-flex flex-wrap gap-2">
        <button class="btn btn-danger" type="button" :disabled="pending" @click="confirmDelete">Підтвердити видалення</button>
        <button class="btn btn-outline-secondary" type="button" :disabled="pending" @click="deleting = null">Скасувати</button>
      </div>
    </section>

    <section class="card surface" aria-labelledby="list-title" :aria-busy="loading">
      <div class="list-header d-flex flex-wrap align-items-center justify-content-between gap-3">
        <div>
          <h2 class="h5 mb-1" id="list-title">Реєстр договорів</h2>
          <span class="muted small" v-if="!loading && !loadError">Записів: {{ filtered.length }}</span>
        </div>
        <div class="search">
          <label for="contract-search" class="visually-hidden">Пошук договорів</label>
          <input id="contract-search" v-model="query" class="form-control" type="search" placeholder="Номер, клієнт або програма" />
        </div>
      </div>

      <p v-if="loading" class="card-body muted mb-0" role="status">Завантаження договорів…</p>
      <p v-else-if="loadError && !contracts.length" class="card-body muted mb-0">Список тимчасово недоступний.</p>
      <p v-else-if="!filtered.length" class="card-body muted mb-0">
        {{ query.trim() ? 'За цим пошуком договорів немає.' : 'У реєстрі поки немає договорів.' }}
      </p>
      <div v-else class="table-responsive" role="region" aria-label="Таблиця договорів" tabindex="0">
        <table class="table align-middle mb-0 contract-table">
          <thead><tr><th scope="col">Договір</th><th scope="col">Клієнт і програма</th><th scope="col">Строк дії</th><th scope="col">Статус</th><th scope="col">Дії</th></tr></thead>
          <tbody>
            <tr v-for="contract in filtered" :key="contract.id">
              <th scope="row">{{ contract.contractNumber }}</th>
              <td>
                {{ clientNames.get(contract.clientId) ?? 'Клієнт №' + contract.clientId }}
                <span class="muted small d-block">{{ planNames.get(contract.insurancePlanId) ?? 'Програма №' + contract.insurancePlanId }}</span>
              </td>
              <td class="date-cell">{{ dateText(contract.startDate) }} – {{ dateText(contract.endDate) }}</td>
              <td><span class="contract-status" :data-status="contract.status">{{ contractStatuses[contract.status] ?? contract.status }}</span></td>
              <td>
                <div class="d-flex flex-wrap gap-2">
                  <button class="btn btn-sm btn-outline-primary" type="button" :disabled="pending" @click="openForm(contract)"
                    :aria-label="'Редагувати ' + contract.contractNumber">Редагувати</button>
                  <button class="btn btn-sm btn-outline-danger" type="button" :disabled="pending" @click="askDelete(contract)"
                    :aria-label="'Видалити ' + contract.contractNumber">Видалити</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <ReferenceSetup v-if="!loading && !loadError" :clients-count="clients.length" :plans-count="plans.length" @created="addReference" />
  </div>
</template>
