# Результати перевірки API

Колекцію виконано через Newman 6.2.2 на Spring Boot 4.0.9, Java 21 і PostgreSQL 17.12. Усі 27 запитів і 81 перевірка пройшли успішно.

| Ресурс | POST | GET список | GET за ID | PUT | GET після PUT | DELETE | GET після DELETE |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Клієнти | 201 | 200 | 200 | 200 | 200 | 204 | 404 |
| Страхові програми | 201 | 200 | 200 | 200 | 200 | 204 | 404 |
| Договори | 201 | 200 | 200 | 200 | 200 | 204 | 404 |

Перевірено відповіді `400` для некоректної пошти, порядку дат і завеликої сторінки. Повторний номер договору і видалення пов’язаного клієнта або програми дають `409`. Після очищення всі три списки в тестовій базі порожні.

Окремий запит `OPTIONS` з `Origin: http://localhost:5173` повернув `200` із заголовком `Access-Control-Allow-Origin: http://localhost:5173`. Некоректний JSON повернув `400` у форматі `application/problem+json`.

## JSON-відповіді для договору

### Створення договору — `POST /api/insurance-contracts` — 201

```json
{
  "id": 1,
  "contractNumber": "MI-a689330f-3c5c-4aaa-a536-128ca56dc58e",
  "startDate": "2027-01-01",
  "endDate": "2027-12-31",
  "status": "DRAFT",
  "clientId": 1,
  "insurancePlanId": 1
}
```

### Оновлення статусу — `PUT /api/insurance-contracts/1` — 200

```json
{
  "id": 1,
  "contractNumber": "MI-a689330f-3c5c-4aaa-a536-128ca56dc58e",
  "startDate": "2027-01-01",
  "endDate": "2027-12-31",
  "status": "ACTIVE",
  "clientId": 1,
  "insurancePlanId": 1
}
```

### Читання збереженого договору — `GET /api/insurance-contracts/1` — 200

```json
{
  "id": 1,
  "contractNumber": "MI-a689330f-3c5c-4aaa-a536-128ca56dc58e",
  "startDate": "2027-01-01",
  "endDate": "2027-12-31",
  "status": "ACTIVE",
  "clientId": 1,
  "insurancePlanId": 1
}
```
