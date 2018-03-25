package Persistance;

import models.testClass;
import play.db.jpa.JPAApi;

import javax.inject.*;
import javax.persistence.*;
import java.util.concurrent.*;
import play.db.jpa.JPAApi;
import javax.inject.Singleton;

@Singleton
public class TestDAO {
    final private JPAApi jpaApi;
    final private DatabaseExecutionContext executionContext;

    @Inject
    public TestDAO(JPAApi api, DatabaseExecutionContext executionContext) {
        this.jpaApi = api;
        this.executionContext = executionContext;
    }

    public CompletableFuture<String> test(String name) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(em -> {
                return (String) em.createNativeQuery("SELECT name FROM public.test where name = '"+name+"';").getSingleResult();
            });
        }, executionContext);
    }

}
