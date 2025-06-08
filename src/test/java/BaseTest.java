import api.generators.TestDataGenerator;
import api.models.Todo;
import api.requests.TodoService;
import api.specs.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    protected TodoService todoService;
    protected Todo todo;

    @BeforeAll
    public static void setupApiTests() {
        RestAssured.baseURI = "http://localhost:8082";
    }

    @BeforeEach
    public void setupTestData() {
        todoService = new TodoService();
        todo = TestDataGenerator.generate(Todo.class);
    }

    @AfterEach
    public void clearData() {
        for (Long createdTodo : todoService.getCreatedTodos()) {
            todoService.delete(Specifications.authSpec(), createdTodo, HttpStatus.SC_NO_CONTENT);
        }
    }
}
