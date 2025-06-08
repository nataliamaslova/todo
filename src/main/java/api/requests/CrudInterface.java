package api.requests;

import api.models.Todo;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public interface CrudInterface {
    void create(Todo todo, int httpStatus);
    void read(Map<String, String> queryParams, int httpStatus);
    void update(long id, Todo todo, int httpStatus);
    void delete(RequestSpecification spec, long id, int httpStatus);
}
