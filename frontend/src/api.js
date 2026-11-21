import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 8000,
  headers: { Accept: 'application/json' },
});

export const contractStatuses = {
  DRAFT: 'Чернетка',
  ACTIVE: 'Активний',
  EXPIRED: 'Завершений',
  CANCELLED: 'Скасований',
};

export async function fetchList(resource, signal) {
  const size = 100;
  const records = [];
  let page = 0;
  let batch;
  do {
    const { data } = await api.get(resource, { params: { page, size }, signal });
    batch = data;
    records.push(...batch);
    page += 1;
  } while (batch.length === size);
  return records;
}

export function createEntry(resource, payload) {
  return api.post(resource, payload).then(({ data }) => data);
}

export function updateContract(id, payload) {
  return api.put('/insurance-contracts/' + id, payload).then(({ data }) => data);
}

export function deleteContract(id) {
  return api.delete('/insurance-contracts/' + id);
}

export function explainError(error) {
  if (!error.response) {
    return 'Не вдалося зв’язатися із сервером. Перевірте, чи запущено Spring Boot, і спробуйте ще раз.';
  }
  const { status, data } = error.response;
  switch (status) {
    case 400: return data?.detail || 'Перевірте, чи правильно заповнено поля.';
    case 404: return 'Запис, клієнта або програму більше не знайдено. Оновіть список.';
    case 409: return 'Запис конфліктує з наявними даними. Перевірте унікальність номера або електронної пошти.';
    default: return 'Сервер повернув помилку HTTP ' + status + '. Спробуйте ще раз.';
  }
}
