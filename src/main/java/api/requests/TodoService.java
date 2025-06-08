package api.requests;

import api.models.Todo;
import api.specs.Specifications;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class TodoService implements CrudInterface {
    public static final String TODOS_END_POINT = "/todos";

    private final List<Long> createdTodos = new ArrayList<>();

    @Override
    public void create(Todo todo, int httpStatus) {
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .body(todo)
                .when()
                .post(TODOS_END_POINT)
                .then()
                .statusCode(httpStatus);

        if (httpStatus == HttpStatus.SC_CREATED) createdTodos.add(todo.getId());
    }

    @Override
    public void read(Map<String, String> queryParams, int httpStatus) {
        RestAssured
                .given()
                .queryParams(queryParams)
                .spec(Specifications.unAuthSpec())
                .when()
                .get(TODOS_END_POINT)
                .then()
                .statusCode(httpStatus);
    }

    @Override
    public void update(long id, Todo todo, int httpStatus) {
        RestAssured
                .given()
                .spec(Specifications.unAuthSpec())
                .body(todo)
                .when()
                .put(TODOS_END_POINT + "/" + id)
                .then()
                .statusCode(httpStatus);
    }

    @Override
    public void delete(RequestSpecification spec, long id, int httpStatus) {
        RestAssured
                .given()
                .spec(spec)
                .when()
                .delete(TODOS_END_POINT + "/" + id)
                .then()
                .statusCode(httpStatus);
    }
}
