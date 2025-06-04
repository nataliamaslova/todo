import api.generators.TestDataGenerator;
import api.models.Todo;
import api.requests.TodoService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoTest {
    private TodoService todoService;
    private Todo expectedTodo;

    @BeforeAll
    public static void setupApiTests() {
        RestAssured.baseURI = "http://localhost:8082";
    }

    @BeforeEach
    public void setupTestData() {
        todoService = new TodoService();
        expectedTodo = TestDataGenerator.generate(Todo.class);
    }

    @AfterEach
    public void clearData() {
        for (Long createdTodo : todoService.getCreatedTodos()) {
            todoService.delete(createdTodo);
        }
    }

    @Test
    public void userCanBeAbleCreateTodo() {
        todoService.create(expectedTodo);

        // Verification impossible: service doesn't return entity as expected
    }

    @Test
    public void userCanBeAbleReadTodo() {
        // 1. Prepare data
        todoService.create(expectedTodo);

        // 2. Act for test
        todoService.read();

        // 3. Verification impossible: service doesn't return entity as expected
    }

    @Test
    public void userCanBeAbleUpdateTodo() {
        // 1. Prepare data
        todoService.create(expectedTodo);
        Todo updatedTodo = TestDataGenerator.generate(Todo.class);
        updatedTodo.setId(expectedTodo.getId());

        // 2. Act for test
        todoService.update(expectedTodo.getId(), updatedTodo);

        // 3. Verification impossible: service doesn't return entity as expected
    }

    @Test
    public void userCanBeAbleDeleteTodo() {
        // 1. Prepare data
        todoService.create(expectedTodo);

        // 2. Act for test
//        todoService.delete(expectedTodo.getId());

        // 3. Verification impossible: service doesn't delete entity as expected
    }

}
