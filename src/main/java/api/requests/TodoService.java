package api.requests;

import api.models.Todo;
import api.specs.Specifications;
import io.restassured.RestAssured;
import lombok.Getter;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TodoService implements CrudInterface {
    public static final String TODOS_END_POINT = "/todos";

    private final List<Long> createdTodos = new ArrayList<>();

    @Override
    public void create(Todo todo) {
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .body(todo)
                .when()
                .post(TODOS_END_POINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        createdTodos.add(todo.getId());
    }

    @Override
    public void read() {
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .when()
                .get(TODOS_END_POINT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public void update(long id, Todo todo) {
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .body(todo)
                .when()
                .put(TODOS_END_POINT + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public void delete(long id) {
        RestAssured
                .given()
                .spec(Specifications.authSpec())
                .when()
                .delete(TODOS_END_POINT + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
