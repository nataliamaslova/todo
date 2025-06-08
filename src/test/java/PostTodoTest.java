import api.generators.TestDataGenerator;
import api.models.Todo;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

/**
 *  POST /todos
 *  Test Cases (Max 5):
 *
 *  1. Create a TODO with valid id, text, and completed
 *  Expect: 201 Created, TODO returned with exact input.
 *
 *  2. Create a TODO with missing field: e.g. completed
 *  Expect: 400 Bad Request
 *
 *  3. Create a TODO with duplicate id
 *  Expect: 400 Bad Request
 *
 *  4. Create a TODO with excessively long text
 *  Expect: 413 PayLoad Too Large
 *
 *  5. Behavior when text is empty
 *
 *  Checklist for Future:
 *  6. Behavior when body is empty
 *  7. Create a TODO with invalid types (e.g. id="abc", completed="yes")
 *  8. Behavior when body is partially malformed JSON
 *  9. Auto-generated ID support (if allowed)
 * 10. Rate limiting on POST
 * 11. Schema validation testing
 */
public class PostTodoTest extends BaseTest {

    // 1. Create a TODO with valid id, text, and completed
    //    Expect: 201 Created, TODO returned with exact input.
    @Test
    public void userCanBeAbleCreateValidTodo() {
        todoService.create(todo, HttpStatus.SC_CREATED);

        // Verification impossible: service doesn't return entity as expected
    }

    //  2. Create a TODO with missing field: e.g. text
    //     Expect: 400 Bad Request
    @Test
    public void userCanBeInformedOnMissedField() {
        Todo invalidTodo = Todo.builder()
                .id(100500)
                .completed(false)
                .build();

        todoService.create(invalidTodo, HttpStatus.SC_BAD_REQUEST);
    }

    //  3. Create a TODO with duplicate id.
    //     Expect: 400 Bad Request
    @Test
    public void userCanBeInformedOnDuplicateId() {
        todoService.create(todo, HttpStatus.SC_CREATED);
        Todo duplicateTodo = Todo.builder()
                .id(todo.getId())
                .text(TestDataGenerator.generateString(15))
                .completed(false)
                .build();

        todoService.create(duplicateTodo, HttpStatus.SC_BAD_REQUEST);
    }

    //  4. Create a TODO with excessively long text
    //     Expect: 413 PayLoad Too Large
    @Test
    public void userCanBeInformedOnTooLongText() {
        todo.setText(TestDataGenerator.generateString(17000));

        todoService.create(todo, HttpStatus.SC_REQUEST_TOO_LONG);
    }

    //  5. Create a TODO with empty text
    //     Expect: 201 Created
    @Test
    public void userCanCreateTodoWithEmptyText() {
        todo.setText("");

        todoService.create(todo, HttpStatus.SC_CREATED);
    }
}
