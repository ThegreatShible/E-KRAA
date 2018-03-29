package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.Test;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;

public class TestDAO2 {
    private DatabaseExecutionContext executionContext;
    private JPAApi jpaApi;

    @Inject
    public TestDAO2(DatabaseExecutionContext executionContext, JPAApi jpaApi) {
        this.executionContext = executionContext;
        this.jpaApi = jpaApi;
    }

    public CompletableFuture<Void> create(final Test test) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {

                /////////////////////////////////////////////////////////////////////////////////////////
                EntityManager em = jpaApi.em();

                em.persist(test);

            });
        }, executionContext);
    }

    public String test() {
        return jpaApi.withTransaction(() -> {
            String str = "Select name from public.test where name = ?";
            return (String) jpaApi.em().createNativeQuery(str).setParameter(1, "hello").getSingleResult();
        });
    }


}
