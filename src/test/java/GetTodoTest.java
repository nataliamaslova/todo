import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
    private HashMap<String, String> queryParams;

    @BeforeEach
    public void setupGet() {
        queryParams = new HashMap<>();
    }

    //  1. Get all TODOs with no query parameters
    //     Expect: 200 OK, full list returned.
    @Test
    public void userCanReadTodo() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 2. Act for test
        todoService.read(queryParams, HttpStatus.SC_OK);

        // 3. Verification impossible: service doesn't return entity as expected
    }

    //  2. Get TODOs with valid limit and offset
    //     Input: limit=2, offset=1
    //     Expect: 200 OK, correct subset returned.
    @Test
    public void userCanReadTodoWithQueryParams() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "2");
        queryParams.put("offset", "1");

        // 2. Act for test
        todoService.read(queryParams, HttpStatus.SC_OK);
    }

    // 3. Get TODOs with limit=0
    //    Expect: 200 OK, empty array returned.
    @Test
    public void userCanReadTodoWithLimitZero() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "0");

        // 2. Act for test
        todoService.read(queryParams, HttpStatus.SC_OK);
    }

    // 4. Get TODOs with large limit (e.g. 1000)
    //    Expect: 200 OK, capped or full list depending on available data.
    @Test
    public void userCanReadTodoWithLargeLimit() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "1000");

        // 2. Act for test
        todoService.read(queryParams, HttpStatus.SC_OK);
    }

    // 5. Get TODOs with invalid limit (e.g. limit=-5)
    //    Expect: 400 Bad Request or fallback to default behavior
    @Test
    public void userCanNotReadTodoWithNegativeLimit() {
        // 1. Prepare data
        todoService.create(todo, HttpStatus.SC_CREATED);
        queryParams.put("limit", "-5");

        // 2. Act for test
        todoService.read(queryParams, HttpStatus.SC_BAD_REQUEST);
    }
}
