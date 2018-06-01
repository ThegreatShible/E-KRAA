package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.users.Group;
import play.db.jpa.JPAApi;
import scala.Option;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupDAO {

    private static String groupCreationQuery = "INSERT INTO public.\"group\"(\n" +
            "\t groupname, owner)\n" +
            "\tVALUES (?, ?) returning groupid";
    private static String groupsSelectionQuery = "SELECT * FROM public.\"group\"";
    private static String getPupilwithGroup = "SELECT  \"user\".userid\n" +
            "\tFROM public.\"user\", auth where groupid = ? and usertype = 'PUPIL' and confirmed = TRUE";
    private static String groupSelectionQuery = "SELECT groupid, groupname, owner\n" +
            "\tFROM public.\"group\" where groupid = ?";
    @Inject
    JPAApi jpaApi;
    @Inject
    DatabaseExecutionContext databaseExecutionContext;
    @Inject
    UserDAO userDAO;

    public CompletableFuture<Integer> createGroup(Group group) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                int id = (int) em.createNativeQuery(groupCreationQuery)
                        .setParameter(1, group.getGroupName())
                        .setParameter(2, group.getOwner().toString()).getSingleResult();
                return id;
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<List<Group>> getGroups() {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> raws = em.createNativeQuery(groupsSelectionQuery).getResultList();

                List<Group> groups = new ArrayList<>();

                for (Object[] raw : raws) {
                    final int groupid = (int) raw[0];
                    List<Object> list = em.createNativeQuery(getPupilwithGroup).setParameter(1, groupid).getResultList();
                    List<UUID> pupilsID = new ArrayList<>();
                    for (Object o : list) {
                        pupilsID.add(UUID.fromString((String) o));
                    }

                    Group group = new Group((int) raw[0], (String) raw[1], pupilsID, UUID.fromString((String) raw[2]));
                    groups.add(group);
                }
                return groups;
            });

        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<Group>> getGroup(int groupid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> raws = em.createNativeQuery(groupSelectionQuery).setParameter(1, groupid).getResultList();
                if (raws.isEmpty()) {
                    return Option.empty();
                } else {
                    Object[] raw = raws.get(0);
                    List<Object> list = em.createNativeQuery(getPupilwithGroup).setParameter(1, groupid).getResultList();
                    List<UUID> pupilsID = new ArrayList<>();
                    for (Object o : list) {
                        pupilsID.add(UUID.fromString((String) o));
                    }
                    Group group = new Group((int) raw[0], (String) raw[1], pupilsID, UUID.fromString((String) raw[2]));
                    return Option.apply(group);
                }
            });
        }, databaseExecutionContext);

    }


}
