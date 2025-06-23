import api.generators.TestDataGenerator;
import api.models.Todo;
import api.specs.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.requests.TodoService.TODOS_END_POINT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *  GET /todos
 *  Test Cases (Max 5):
 *
 *  1. Get all TODOs with no query parameters
 *  Expect: 200 OK, full list returned.
 *
 *  2. Get TODOs with valid limit and offset
 *  Input: limit=2, offset=1
 *  Expect: 200 OK, correct subset returned.
 *
 *  3. Get TODOs with limit=0
 *  Expect: 200 OK, empty array returned.
 *
 *  4. Get TODOs with large limit (e.g. 1000)
 *  Expect: 200 OK, capped or full list depending on available data.
 *
 *  5. Get TODOs with invalid limit (e.g. limit=-5)
 *  Expect: 400 Bad Request or fallback to default behavior.
 *
 *  Checklist for Future:
 *
 *  6. Pagination consistency when creating/deleting during fetch
 *  7. Behavior when DB is empty
 *  8. Performance under high data volume
 *  9. Security: ensure no sensitive data leaks
 * 10. Invalid query param types (e.g. limit=abc)
 */
public class GetTodoTest extends BaseTest {

    //  1. Get all TODOs with no query parameters
    //     Expect: 200 OK, full list returned.
    @Test
    public void userCanReadTodo() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);

        // 3. Assert
        assertThat(actualTodos).hasSize(1);
        assertThat(actualTodos).isEqualTo(List.of(todo));
    }

    //  2. Get TODOs with valid limit and offset
    //     Input: limit=2, offset=1
    //     Expect: 200 OK, correct subset returned.
    @Test
    public void userCanReadTodoWithQueryParams() {
        // 1. Arrange
        Todo todo2 = TestDataGenerator.generate(Todo.class);
        Todo todo3 = TestDataGenerator.generate(Todo.class);
        todoService.create(todo, HttpStatus.SC_CREATED);
        todoService.create(todo2, HttpStatus.SC_CREATED);
        todoService.create(todo3, HttpStatus.SC_CREATED);

        queryParams.put("limit", "2");
        queryParams.put("offset", "1");

        // 2. Act
        List<Todo> todos = todoService.read(queryParams, HttpStatus.SC_OK);

        // 3. Assert
        assertThat(todos).hasSize(2);
        assertThat(todos)
                .extracting(Todo::getText)
                .containsExactly(todo2.getText(), todo3.getText()); // если знаешь какой offset
    }

    // 3. Get TODOs with limit=0
    //    Expect: 200 OK, empty array returned.
    @Test
    public void userCanReadTodoWithLimitZero() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "0");

        // 2. Act
        List<Todo> todos = todoService.read(queryParams, HttpStatus.SC_OK);

        // 3. Assert
        assertThat(todos).isEmpty();
    }

    // 4. Get TODOs with large limit (e.g. 100)
    //    Expect: 200 OK, capped or full list depending on available data.
    @Test
    public void userCanReadTodoWithLargeLimit() {
        // 1. Arrange
        for (int i = 0; i < REQUESTS_COUNT + 2; i++) {
            todo.setId(i);
            todo.setText("test_" + i);
            todoService.create(todo, HttpStatus.SC_CREATED);
        }
        queryParams.put("limit", Integer.toString(REQUESTS_COUNT));

        // 2. Act
        List<Todo> todos = todoService.read(queryParams, HttpStatus.SC_OK);

        // 3. Assert
        assertThat(todos).hasSize(REQUESTS_COUNT);
    }

    // 5. Get TODOs with invalid limit (e.g. limit=-5)
    //    Expect: 400 Bad Request or fallback to default behavior
    @Test
    public void userCanNotReadTodoWithNegativeLimit() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "-5");

        // 2. Act and 3. Assert status code
        RestAssured
                .given()
                .queryParams(queryParams)
                .spec(Specifications.unAuthSpec())
                .when()
                .get(TODOS_END_POINT)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
