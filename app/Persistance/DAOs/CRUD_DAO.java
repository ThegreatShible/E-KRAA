package Persistance.DAOs;


import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @param <A> object type
 * @param <T> primary key type
 */

public interface CRUD_DAO<A, T> {
    CompletableFuture<T> create(A obj);

    CompletableFuture<Void> edit(A obj);

    CompletableFuture<Void> destroy(T key);

    CompletableFuture<A> find(T key);

    CompletableFuture<Integer> count();

    CompletableFuture<List<A>> findAll();

    CompletableFuture<List<A>> find(int max, int first);

}
