package api;

import api.generators.TestDataGenerator;
import api.models.Todo;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void userCanCreateValidTodo() {
        // Act
        todoService.create(todo, HttpStatus.SC_CREATED);

        // Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).hasSize(1);
        assertThat(actualTodos).isEqualTo(List.of(todo));
    }

    //  2. Create a TODO with missing field: e.g. text
    //     Expect: 400 Bad Request
    @Test
    public void userCanBeInformedAboutMissedField() {
        // 1. Arrange
        Todo invalidTodo = Todo.builder()
                .id(100500)
                .completed(false)
                .build();

        // 2. Act
        todoService.create(invalidTodo, HttpStatus.SC_BAD_REQUEST);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).isEmpty();
    }

    //  3. Create a TODO with duplicate id.
    //     Expect: 400 Bad Request
    @Test
    public void userCanBeInformedOnDuplicateId() {
        // 1. Arrange
        todoService.create(todo, HttpStatus.SC_CREATED);
        Todo duplicateTodo = Todo.builder()
                .id(todo.getId())
                .text(TestDataGenerator.generateString(15))
                .completed(false)
                .build();

        // 2. Act
        todoService.create(duplicateTodo, HttpStatus.SC_BAD_REQUEST);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).hasSize(1);
    }

    //  4. Create a TODO with excessively long text
    //     Expect: 413 PayLoad Too Large
    @Test
    public void userCanBeInformedAboutTooLongText() {
        // 1. Arrange
        todo.setText(TestDataGenerator.generateString(17000));

        // 2. Act
        todoService.create(todo, HttpStatus.SC_REQUEST_TOO_LONG);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).isEmpty();
    }

    //  5. Create a TODO with empty text
    //     Expect: 201 Created
    @Test
    public void userCanCreateTodoWithEmptyText() {
        // 1. Arrange
        todo.setText("");

        // 2. Act
        todoService.create(todo, HttpStatus.SC_CREATED);

        // 3. Assert
        List<Todo> actualTodos = todoService.read(queryParams, HttpStatus.SC_OK);
        assertThat(actualTodos).hasSize(1);
        assertThat(actualTodos).isEqualTo(List.of(todo));
    }
}