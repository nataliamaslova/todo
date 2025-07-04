##  API testing

The attached image contains an application implementing the most straightforward TODO manager with CRUD operations. The image can be loaded via docker load and run using:
docker run -p 8082:4242 -e VERBOSE=1 todo-app.

The task includes two parts:

Firstly, it's required to write some api for checking the functionality of the application. We don't provide strict specifications because the domain is simple enough. Therefore, it is necessary to come up with cases by yourself.
For each route, implement a maximum of 5 test cases. Any additional test cases should be provided as a checklist for future implementation.

Secondly, it's necessary to check the performance of the POST /todos endpoint. It's not required to draw graphs. Measurements and some summary are enough.

## Endpoints
The only entity here is TODO represented by a structure with the following three fields:
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

## Summary

### 1. Functional testing
Implemented 20 api for CRUD operations:

api.PostTodoTest (Create)
- ✅ userCanCreateValidTodo
- ✅ userCanBeInformedAboutMissedField
- ✅ userCanBeInformedOnDuplicateId
- ✅ userCanBeInformedAboutTooLongText
- ✅ userCanCreateTodoWithEmptyText

api.GetTodoTest (Read)
- ✅ userCanReadTodo
- ✅ userCanReadTodoWithQueryParams
- ✅ userCanReadTodoWithLimitZero
- ✅ userCanReadTodoWithLargeLimit
- ✅ userCanNotReadTodoWithNegativeLimit

api.PutTodoTest (Update)
- ✅ userCanUpdateTodo
- ✅ userCanNotUpdateNonExistentTodo
- 🚨 userCanNotUpdateTodoWithIncompleteBody
- 🚨 userCanNotUpdateTodoWithInvalidTypeFormat
- ✅ userCanUpdateTodoWithEmptyText

api.DeleteTodoTest (Delete)
- ✅ userCanDeleteTodo
- ✅ userCanNotDeleteNonExistentTodo
- ✅ userCanNotDeleteTodoWithoutAuthorization
- ✅ userCanNotDeleteTodoWithInvalidCredentials
- 🚨 userCanNotDeleteTodoWithMalformedId

At functional testing of CRUD methods the bugs are detected:
1. DELETE TODO with malformed ID (e.g. /todos/abc)
   Expected: 400 Bad Request, but actual: 404 Not Found
2. UPDATE TODO with invalid Type format (e.g. string instead of boolean for completed)
   Expect: 400 Bad Request, but actual: 401 Unauthorized
3. UPDATE TODO with incomplete body (e.g. missing text)
   Expect: 400 Bad Request, but actual: 401 Unauthorized

### 2. Performance testing
- api.LogTimeExtractor
Test analysed logs of application.
1000 POST requests selected.
Average response time is 106 µs.
No errors were found.
Under sequential load, the server remains stable, the time does not increase noticeably.
Conclusion: POST /todos performance is within normal limits for the basic load.

- api.PostPerformanceTest
Test sent 1000 POST requests.
Average response time is 12258 µs.

🔍 К выяснению. При выполнении тестирования возник вопрос о времени выполнения POST-запросов:
с помощью Rest Assured среднее время выполнения запроса POST (1000 запросов): 12258 µs.
При таких же условиях по информации из логов приложения среднее время выполнения запроса POST (1000 запросов): 106.1 µs.
❓ Почему такое существенное отличие: на 2 порядка?

Data for 100 POST requests:
- Rest Assured:
Max POST time: 2073004.2
Min POST time: 14187.4
Average POST time: 45418.67
POST duration: [2073004.2, 46609.1, 36836.3, 32524.6, 24790.1, 26820.1, 26177.6, 37650.3, 31276.6, 34140.1, 31579.8, 27285.3, 30489.4, 36866.7, 30014.4, 67353.4, 27755.7, 25914.8, 23202.0, 31480.5, 27233.2, 24934.4, 30098.1, 20220.7, 18859.8, 26746.0, 25278.4, 31357.9, 31108.5, 30444.4, 29924.7, 22169.6, 19523.8, 64875.5, 24751.0, 23767.5, 25181.0, 35453.3, 20284.4, 16350.3, 21977.3, 18635.7, 24408.7, 16668.1, 23629.6, 24776.3, 23388.9, 25127.6, 22649.4, 20755.1, 22167.1, 22230.2, 19907.3, 17311.9, 21874.8, 19740.3, 20735.4, 23752.0, 18157.1, 18584.5, 19420.3, 16569.4, 20336.5, 20660.2, 26087.0, 20446.1, 22973.9, 22233.9, 16992.8, 16835.3, 16089.3, 19304.7, 16472.5, 17398.9, 18679.9, 21946.5, 19136.9, 18616.7, 17955.7, 15232.5, 20278.0, 18603.1, 21297.9, 21915.9, 28007.3, 25099.9, 22444.7, 17248.4, 14187.4, 15812.9, 20275.6, 86837.7, 21670.5, 24189.6, 23628.8, 25847.7, 23599.2, 16697.1, 16836.0, 17189.9]


- Logs:
Max POST time: 405.56
Min POST time: 74.11
Average POST time: 143.18
POST duration: [405.564, 103.183, 100.997, 108.821, 104.271, 74.106, 99.606, 119.479, 115.091, 250.51, 115.231, 150.462, 146.817, 173.558, 122.263, 149.545, 120.586, 196.009, 144.939, 148.625, 118.073, 119.758, 204.602, 128.332, 128.678, 110.301, 96.937, 112.193, 203.913, 122.885, 171.556, 148.507, 129.647, 141.006, 215.395, 122.239, 141.164, 215.823, 94.107, 107.418, 173.224, 104.903, 215.508, 132.583, 103.798, 132.44, 136.208, 121.998, 129.027, 140.21, 102.548, 120.415, 127.871, 139.186, 245.83, 113.37, 153.075, 222.42, 94.354, 114.491, 147.966, 194.031, 157.613, 120.272, 175.2, 113.108, 107.495, 168.001, 204.845, 120.641, 133.421, 132.28, 187.404, 240.51, 80.806, 121.858, 115.752, 108.486, 161.08, 142.532, 100.218, 142.091, 146.314, 164.67, 120.2, 155.039, 151.487, 100.934, 76.676, 128.946, 107.712, 80.615, 148.853, 167.9, 123.724, 350.281, 178.314, 113.274, 108.744, 113.095]
