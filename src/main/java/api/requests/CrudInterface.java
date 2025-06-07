package api.requests;

import api.models.Todo;

public interface CrudInterface {
    void create(Todo todo);
    void read();
    void update(long id, Todo todo);
    void delete(long id);
}
