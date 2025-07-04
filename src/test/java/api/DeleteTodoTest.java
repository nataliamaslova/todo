package api;

import api.models.Todo;
import api.specs.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.requests.TodoService.TODOS_END_POINT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *  DELETE /todos/:id
 * ⚠️ Requires Basic Auth (admin:admin)
 *
 *  Test Cases (Max 5):
 *
 *  1. Delete existing TODO with correct credentials
 *  Expect: 204 No Content
 *
 *  2. Delete non-existent TODO with correct credentials
 *  Expect: 404 Not Found
 *
 *  3. Delete TODO without Authorization header
 *  Expect: 401 Unauthorized
 *
 *  4. Delete TODO with invalid credentials
 *  Expect: 401 Unauthorized
 *
 *  5. Delete TODO with malformed ID (e.g. /todos/abc)
 *  Expect: 400 Bad Request
 *
 * Checklist for Future:
 *  6. Attempt delete on already deleted resource
 *  7. Attempt batch delete (if unsupported)
 *  8. Rate limit for delete operations
 *  9. Check response when no content is returned
 */
public class DeleteTodoTest extends BaseTest {

    @Test
    public void userCanDeleteTodo() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);

        // Hook In order not to delete twice due to clearData at @AfterEach at tests.BaseTest
        List<Long> createdTodos = todoService.getCreatedTodos();
        createdTodos.remove(todo.getId());
        todoService.setCreatedTodos(createdTodos);

        // 2. Act
        todoService.delete(Specifications.authSpec(), todo.getId(), HttpStatus.SC_NO_CONTENT);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).isEmpty();

//        RestAssured.given()
//                .spec(Specifications.authSpec())
//                .when()
//                .get(TODOS_END_POINT + "/" + todo.getId())
//                .then()
//                .statusCode(HttpStatus.SC_NOT_FOUND); // but 405 Method Not Allowed: instead of 404
    }

    //  2. Delete non-existent TODO with correct credentials
    //     Expect: 404 Not Found
    @Test
    public void userCanNotDeleteNonExistentTodo() {
        todoService.delete(Specifications.authSpec(), todo.getId(), HttpStatus.SC_NOT_FOUND);
    }

    // 3. Delete TODO without Authorization header
    //    Expect: 401 Unauthorized
    @Test
    public void userCanNotDeleteTodoWithoutAuthorization() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act
        todoService.delete(Specifications.unAuthSpec(), todo.getId(), HttpStatus.SC_UNAUTHORIZED);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).extracting(Todo::getId)
                .containsExactly(todo.getId());
    }

    // 4. Delete TODO with invalid credentials
    //    Expect: 401 Unauthorized
    @Test
    public void userCanNotDeleteTodoWithInvalidCredentials() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act
        todoService.delete(Specifications.invalidAuthSpec(), todo.getId(), HttpStatus.SC_UNAUTHORIZED);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).extracting(Todo::getId)
                .containsExactly(todo.getId());
    }

    // 5. Delete TODO with malformed ID (e.g. /todos/abc)
    //    Expect: 400 Bad Request
    @Test
    public void userCanNotDeleteTodoWithMalformedId() {
        // 2. Act and 3. Assert status code
        RestAssured
                .given()
                .spec(Specifications.authSpec())
                .when()
                .delete(TODOS_END_POINT + "/" + "abc")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        // Expected: 400 Bad Request, but Actual: 404: Not Found
    }
}
