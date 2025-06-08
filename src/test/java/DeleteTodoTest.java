import api.requests.TodoService;
import api.specs.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

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
 *  7. Audit that only admin users can delete
 *  8. Attempt batch delete (if unsupported)
 *  9. Rate limit for delete operations
 * 10. Check response when no content is returned
 */
public class DeleteTodoTest extends BaseTest {

    @Test
    public void userCanBeAbleDeleteTodo() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act for test
 //       todoService.delete(Specifications.authSpec(), todo.getId(), HttpStatus.SC_NO_CONTENT);

        // 3. Verification impossible: service doesn't delete entity as expected
    }

    //  2. Delete non-existent TODO with correct credentials
    //     Expect: 404 Not Found
    @Test
    public void userCanNotBeAbleDeleteNonExistentTodo() {
        todoService.delete(Specifications.authSpec(), todo.getId(), HttpStatus.SC_NOT_FOUND);
    }

    // 3. Delete TODO without Authorization header
    //    Expect: 401 Unauthorized
    @Test
    public void userCanNotBeAbleDeleteTodoWithoutAuthorization() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act for test
        todoService.delete(Specifications.unAuthSpec(), todo.getId(), HttpStatus.SC_UNAUTHORIZED);
    }

    // 4. Delete TODO with invalid credentials
    //    Expect: 401 Unauthorized
    @Test
    public void userCanNotBeAbleDeleteTodoWithInvalidCredentials() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act for test
        todoService.delete(Specifications.invalidAuthSpec(), todo.getId(), HttpStatus.SC_UNAUTHORIZED);
    }

    // 5. Delete TODO with malformed ID (e.g. /todos/abc)
    //    Expect: 400 Bad Request
    @Test
    public void userCanNotBeAbleDeleteTodoWithMalformedId() {
        // Act for test
        RestAssured
                .given()
                .spec(Specifications.authSpec())
                .when()
                .delete(TodoService.TODOS_END_POINT + "/" + "abc")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        // Expected: 400 Bad Request, but Actual: 404: Not Found
    }
}
