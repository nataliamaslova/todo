Тестирование API

Покрыть CRUD todo сервис.

The attached image contains an application implementing the most straightforward TODO manager with CRUD operations. The image can be loaded via docker load and run using docker run -p 8080:4242.

## Endpoints
The only entity here is TODO  represented by a structure with the following three fields:
* id — an unsigned 64-bit identifier
* text - description of TODO
* completed - whether the todo is completed or not

### GET /todos

Get a JSON list of TODOs.

Available query parameters:
* offset — how many TODOs should be skipped
* limit - the maximum number of TODOs to be returned

### POST /todos

Create a new TODO. This endpoint expects the whole TODO structure as the request body.

### PUT /todos/:id

Update an existing TODO with the provided one.

### DELETE /todos/:id

Delete an existing TODO.

The endpoint requires the Authorization request header containing the admin:admin credentials in the Basic authorization schema.

PS. Учитывая, что сервис не возвращает данные как ожидается,
мне не удалось реализовать best practices для необходимых проверок (ассертов).
Также интерфейс CRUD реализован в соответствии с текущей функциональностью сервиса todo
(нет возможности использовать созданные сущности).
Реализовано 4 теста на CRUD в классе TodoTest:
- userCanBeAbleCreateTodo
- userCanBeAbleReadTodo
- userCanBeAbleUpdateTodo
- userCanBeAbleDeleteTodo