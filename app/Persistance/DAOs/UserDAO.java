package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import Persistance.PasswordHasher;
import Persistance.UserType;
import models.users.Pupil;
import models.users.Teacher;
import models.users.User;
import org.apache.commons.codec.digest.DigestUtils;
import play.db.jpa.JPAApi;
import scala.Option;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserDAO {
    private static String insertTeacher = "INSERT INTO public.\"user\"(\n" +
            "\tuserid, firstname, lastname, birthdate, gender, photolink, usertype, score, level, email,groupid)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, NULL, NULL, ?, NULL) returning userid";
    private static String insertPupil = "INSERT INTO public.\"user\"(\n" +
            "\tuserid, firstname, lastname, birthdate, gender, photolink, usertype, score, level, email, groupid)\n" +
            "\tVALUES (?, ?, ?, ?, ?, NULL, ?, ?, ?, ?, ?) returning userid";
    private static String insertAuth = "INSERT INTO public.auth(\n" +
            "\tuserid, password, confirmed)\n" +
            "\tVALUES (?, ?, FALSE)";
    private static String insertPupilAuth = "INSERT INTO public.auth(\n" +
            "\tuserid, password, confirmed)\n" +
            "\tVALUES (?, NULL, FALSE)";
    private static String selectTeacherbyMail = "SELECT \"user\".userid, firstname, lastname, birthdate, gender, photolink, email\n" +
            "\tFROM public.\"user\", auth where email = ? and usertype = 'TEACHER' and confirmed = TRUE";
    private static String selectTeacherbyId = "SELECT \"user\".userid, firstname, lastname, birthdate, gender, photolink, email\n" +
            "\tFROM public.\"user\", auth where userid = ? and usertype = 'TEACHER' and confirmed = TRUE";
    private static String selectPupilbyMail = "SELECT \"user\".userid, firstname, lastname, birthdate, gender, photolink,  score, level, email, groupid\n" +
            "\tFROM public.\"user\", auth where email = ? and usertype = 'PUPIL' and confirmed = TRUE";
    private static String selectPupilbyId = "SELECT \"user\".userid, firstname, lastname, birthdate, gender, photolink,  score, level, email,groupid\n" +
            "\tFROM public.\"user\", auth where userid = ? and usertype = 'PUPIL' and confirmed = TRUE";
    private static String selectPupilsbyGroup = "SELECT \"user\".userid, firstname, lastname, birthdate, gender, photolink,  score, level, email,groupid\n" +
            "\tFROM public.\"user\", auth where groupid = ? and usertype = 'PUPIL' and confirmed = TRUE";

    private static String selectAuth = "SELECT userid, password\n" +
            "\tFROM public.auth where userid = ?";
    private static String confirmQuery = "update auth set confirmed = TRUE where userid = ?";

    private static String selectUserbyMail = "SELECT \"user\".userid as uid, firstname, lastname," +
            "birthdate,gender,photolink,usertype,score,level,email,auth.userid as ide, password FROM public.\"user\", public.auth where email = ? and confirmed = TRUE";
    private static String setPupilGroupQuery = "UPDATE public.\"user\"\n" +
            "\tSET  groupid=?\n" +
            "\tWHERE userid = ?";

    private static String insertToken = "INSERT INTO public.confirmtoken(token, userid) VALUES (?, ?)";
    private static String userIDFromToken = "SELECT userid from confirmtoken where token = ?";

    private JPAApi jpaApi;
    private DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public UserDAO(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }

    public CompletableFuture<UUID> createTeacher(final Teacher teacher, final String notHashedPassword) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                String hashedPassword = PasswordHasher.md5Hash(notHashedPassword);
                EntityManager em = jpaApi.em();
                String userid = (String) em.createNativeQuery(insertTeacher).setParameter(1, teacher.getId().toString())
                        .setParameter(2, teacher.getFirstName())
                        .setParameter(3, teacher.getLastName()).setParameter(4, teacher.getBirthDate(), TemporalType.DATE)
                        .setParameter(5, teacher.getGender().toString())
                        .setParameter(6, teacher.getPhotoLink())
                        .setParameter(7, UserType.TEACHER.toString())
                        .setParameter(8, teacher.getEmail()).getSingleResult();
                em.createNativeQuery(insertAuth).setParameter(1, userid)
                        .setParameter(2, hashedPassword).executeUpdate();
                UUID tokenid = UUID.randomUUID();
                em.createNativeQuery(insertToken).setParameter(1, tokenid.toString())
                        .setParameter(2, userid)
                        .executeUpdate();
                return tokenid;

            });
        }, databaseExecutionContext);


    }

    public CompletableFuture<UUID> createPupil(final Pupil pupil) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                System.out.println("ooooooooooooooooooooooooooooooooo  " + pupil.getGroupID());
                EntityManager em = jpaApi.em();
                String userid = (String) em.createNativeQuery(insertPupil).setParameter(1, pupil.getId().toString())
                        .setParameter(2, pupil.getFirstName())
                        .setParameter(3, pupil.getLastName()).setParameter(4, pupil.getBirthDate(), TemporalType.DATE)
                        .setParameter(5, pupil.getGender().toString())
                        .setParameter(6, UserType.PUPIL.toString())
                        .setParameter(7, pupil.getScore())
                        .setParameter(8, pupil.getLevel())
                        .setParameter(9, pupil.getEmail())
                        .setParameter(10, pupil.getGroupID())
                        .getSingleResult();
                System.out.println("hhhhhhhhhhhhhhhheeeeeeeeeeerrrrrrrrrrrrreeeeeeeeeeh  " + pupil.getGroupID());
                em.createNativeQuery(insertPupilAuth).setParameter(1, userid).executeUpdate();
                //token for verifying
                UUID tokenid = UUID.randomUUID();
                em.createNativeQuery(insertToken).setParameter(1, tokenid.toString())
                        .setParameter(2, userid)
                        .executeUpdate();
                return tokenid;

            });
        }, databaseExecutionContext);

    }

    public CompletableFuture<Option<Teacher>> getTeacher(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                try {
                    Teacher teacher = (Teacher) em.createNativeQuery(selectTeacherbyId, Teacher.class)
                            .setParameter(1, uuid.toString()).getSingleResult();
                    return Option.apply(teacher);
                } catch (Exception e) {
                    return Option.empty();
                }
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<Teacher>> getTeacher(String mail) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                try {
                    Teacher teacher = (Teacher) em.createNativeQuery(selectTeacherbyMail, Teacher.class)
                            .setParameter(1, mail.toString()).getSingleResult();
                    return Option.apply(teacher);
                } catch (Exception e) {
                    return Option.empty();
                }
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<Pupil>> getPupil(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                try {
                    Pupil pupil = (Pupil) em.createNativeQuery(selectPupilbyId, Pupil.class)
                            .setParameter(1, uuid.toString()).getSingleResult();
                    return Option.apply(pupil);
                } catch (Exception e) {
                    return Option.empty();
                }
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<List<Pupil>> getPupilsByGroup(int groupid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> rawPupils = em.createNativeQuery(selectPupilsbyGroup, Pupil.class)
                        .setParameter(1, groupid).getResultList();
                List<Pupil> pupils = new ArrayList<>();
                for (Object[] raw : rawPupils) {
                    Pupil pupil = new Pupil();
                    pupil.setId(UUID.fromString((String) raw[0]));
                    pupil.setFirstName((String) raw[1]);
                    pupil.setLastName((String) raw[2]);
                    pupil.setBirthDate((Date) raw[3]);
                    pupil.setGender((String) raw[4]);
                    pupil.setScore((int) raw[7]);
                    pupil.setLevel((short) raw[8]);
                    pupil.setEmail((String) raw[9]);
                    pupil.setGroupID((int) raw[10]);
                    pupils.add(pupil);
                }
                return pupils;

            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<Pupil>> getPupil(String email) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                try {
                    Pupil pupil = (Pupil) em.createNativeQuery(selectPupilbyMail, Pupil.class)
                            .setParameter(1, email).getSingleResult();
                    return Option.apply(pupil);
                } catch (Exception e) {
                    return Option.empty();
                }
            });
        }, databaseExecutionContext);
    }


    //TODO : Authentication is just for teacher
    public CompletableFuture<Option<User>> authenticate(String email, String notHashedPassword) {
        String hashedPassword = DigestUtils.md5Hex(notHashedPassword);
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> objs = em.createNativeQuery(selectUserbyMail).setParameter(1, email)
                        .getResultList();
                if (!objs.isEmpty()) {
                    Object[] obj = objs.get(0);
                    if (obj[11].equals(hashedPassword)) {

                        if (obj[6].equals("PUPIL")) {
                            Pupil pupil = new Pupil();
                            pupil.setId(UUID.fromString((String) obj[0]));
                            pupil.setFirstName((String) obj[1]);
                            pupil.setLastName((String) obj[2]);
                            pupil.setBirthDate((Date) obj[3]);
                            pupil.setGender((String) obj[4]);
                            pupil.setScore((int) obj[7]);
                            pupil.setLevel((short) obj[8]);
                            pupil.setEmail((String) obj[9]);
                            return Option.apply(pupil);
                        } else {
                            Teacher teacher = new Teacher();
                            teacher.setId(UUID.fromString((String) obj[0]));
                            teacher.setFirstName((String) obj[1]);
                            teacher.setLastName((String) obj[2]);
                            teacher.setBirthDate((Date) obj[3]);
                            teacher.setGender((String) obj[4]);
                            teacher.setPhotoLink((String) obj[5]);
                            teacher.setEmail((String) obj[9]);
                            return Option.apply(teacher);
                        }
                    } else {
                        return Option.empty();
                    }
                } else return Option.empty();


            });

        }, databaseExecutionContext);

    }

    public CompletableFuture<Integer> setPupilGroup(int groupid, UUID userid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                return em.createNativeQuery(setPupilGroupQuery).setParameter(1, groupid)
                        .setParameter(2, userid.toString()).executeUpdate();
            });
        }, databaseExecutionContext);
    }

    private CompletableFuture<UUID> confirmUser(UUID userID) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                jpaApi.em().createNativeQuery(confirmQuery).setParameter(1, userID.toString()).executeUpdate();
                return userID;
            });
        }, databaseExecutionContext);
    }

    private CompletableFuture<Option<UUID>> getUserFromToken(UUID token) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<String> list = em.createNativeQuery(userIDFromToken)
                        .setParameter(1, token.toString()).getResultList();
                if (list.isEmpty()) return Option.empty();
                else {
                    UUID uid = UUID.fromString((String) list.get(0));
                    return Option.apply(uid);
                }
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<UUID>> confirmUserByToken(UUID token) {
        return getUserFromToken(token).thenCompose(op -> {
            if (op.isEmpty()) {
                return CompletableFuture.completedFuture(Option.empty());
            } else {
                UUID userID = op.get();
                confirmUser(userID);
                return CompletableFuture.completedFuture(Option.apply(userID));
            }

        });
    }

}
