import api.generators.TestDataGenerator;
import api.models.Todo;
import api.requests.TodoService;
import api.specs.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PUT /todos/:id
 * Test Cases (Max 5):
 * 1. Update existing TODO by ID with valid fields
 * Expect: 200 OK, updated TODO returned.
 *
 * 2. Update TODO that does not exist
 * Expect: 404 Not Found
 *
 * 3. Update TODO with incomplete body (e.g. missing text)
 * Expect: 400 Bad Request
 *
 * 4. Update TODO with invalid Type format (e.g. string instead of boolean for completed)
 * Expect: 400 Bad Request
 *
 * 5. Update TODO with valid ID but empty text
 * Expect: 200 OK or validation error (depends on business rules)
 *
 * Checklist for Future:
 * 6. Concurrent update handling
 * 7. Validation of field immutability (if any)
 * 8. Behavior when extra fields are provided
 * 9. Partial updates (if supported)
 * 10. Audit/logging verification (if applicable)
 */
public class PutTodoTest extends BaseTest {

    //  1. Update existing TODO by ID with valid fields
    //     Expect: 200 OK, updated TODO returned.
    @Test
    public void userCanUpdateTodo() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);
        Todo updatedTodo = TestDataGenerator.generate(Todo.class);
        updatedTodo.setId(todo.getId());

        // 2. Act
        todoService.update(todo.getId(), updatedTodo, HttpStatus.SC_OK);

        // 3. Assert
        List<Todo> todos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(todos).isEqualTo(List.of(updatedTodo));
    }

    // 2. Update TODO that does not exist
    //    Expect: 404 Not Found
    @Test
    public void userCanNotUpdateNonExistentTodo() {
        // 1. Arrange
        Todo updatedTodo = TestDataGenerator.generate(Todo.class);

        // 2. Act and 3. Assert status code
        todoService.update(updatedTodo.getId(), updatedTodo, HttpStatus.SC_NOT_FOUND);
    }

    // 3. Update TODO with incomplete body (e.g. missing text)
    // Expect: 400 Bad Request
    @Test
    public void userCanNotUpdateTodoWithIncompleteBody() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);
        Todo invalidTodo = Todo.builder()
                .id(todo.getId())
                .completed(false)
                .build();

        // 2. Act and 3. Assert status code
        todoService.update(todo.getId(), invalidTodo, HttpStatus.SC_BAD_REQUEST);

        // 400 Bad Request expected, but actual: 401 Unauthorized
    }

    // 4. Update TODO with invalid Type format (e.g. string instead of boolean for completed)
    //    Expect: 400 Bad Request
    @Test
    public void userCanNotUpdateTodoWithInvalidTypeFormat() {
        // 1. Arrange
        final long id = 100500;
        todo.setId(id);
        todoService.create(todo, HttpStatus.SC_CREATED);

        String updatedTodo = """
                {
                  "id": 100500,
                  "text": "first updated post",
                  "completed": "yes"
                }
                """;

        // 2. Act and 3. Assert status code
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .body(updatedTodo)
                .when()
                .put(TodoService.TODOS_END_POINT + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        // 400 Bad Request expected, but actual: 401 Unauthorized
    }


    //  5. Update TODO with valid ID but empty text
    //     Expect: 200 OK or validation error (depends on business rules)
    @Test
    public void userCanUpdateTodoWithEmptyText() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);
        Todo updatedTodo = TestDataGenerator.generate(Todo.class);
        updatedTodo.setId(todo.getId());
        updatedTodo.setText("");

        // 2. Act
        todoService.update(todo.getId(), updatedTodo, HttpStatus.SC_OK);

        // 3. Assert
        List<Todo> todos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(todos).isEqualTo(List.of(updatedTodo));
    }
}
