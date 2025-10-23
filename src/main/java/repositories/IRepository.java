package repositories;

import java.util.List;

import com.mongodb.client.result.InsertOneResult;

public interface IRepository<T> {
    InsertOneResult save(T entity);
    T findById(String id);
    List<T> findAll();
    void delete(String id);
}