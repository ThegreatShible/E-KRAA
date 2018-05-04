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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserDAO {
    private static String insertTeacher = "INSERT INTO public.\"user\"(\n" +
            "\tuserid, firstname, lastname, birthdate, gender, photolink, usertype, score, level, email)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, NULL, NULL, ?) returning userid";
    private static String insertPupil = "INSERT INTO public.\"user\"(\n" +
            "\tuserid, firstname, lastname, birthdate, gender, photolink, usertype, score, level, email)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning userid";
    private static String insertAuth = "INSERT INTO public.auth(\n" +
            "\tuserid, password)\n" +
            "\tVALUES (?, ?)";
    private static String selectTeacherbyMail = "SELECT userid, firstname, lastname, birthdate, gender, photolink, email\n" +
            "\tFROM public.\"user\" where email = ? and usertype = 'TEACHER'";
    private static String selectTeacherbyId = "SELECT userid, firstname, lastname, birthdate, gender, photolink, email\n" +
            "\tFROM public.\"user\" where userid = ? and usertype = 'TEACHER'";
    private static String selectPupilbyMail = "SELECT userid, firstname, lastname, birthdate, gender, photolink,  score, level, email\n" +
            "\tFROM public.\"user\" where email = ? and usertype = 'PUPIL'";
    private static String selectPupilbyId = "SELECT userid, firstname, lastname, birthdate, gender, photolink,  score, level, email\n" +
            "\tFROM public.\"user\" where userid = ? and usertype = 'PUPIL'";
    private static String selectAuth = "SELECT userid, password\n" +
            "\tFROM public.auth where userid = ?";
    private static String selectUserbyMail = "SELECT \"user\".userid as uid, firstname, lastname," +
            "birthdate,gender,photolink,usertype,score,level,email,auth.userid as ide, password FROM public.\"user\", public.auth where email = ?";
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
                em.createNativeQuery(insertAuth).setParameter(1, userid).setParameter(2, hashedPassword).executeUpdate();
                return UUID.fromString(userid);

            });
        }, databaseExecutionContext);


    }

    public CompletableFuture<UUID> createPupil(final Pupil pupil, final String notHashedPassword) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                String hashedPassword = PasswordHasher.md5Hash(notHashedPassword);
                EntityManager em = jpaApi.em();
                String userid = (String) em.createNativeQuery(insertPupil).setParameter(1, pupil.getId().toString())
                        .setParameter(2, pupil.getFirstName())
                        .setParameter(3, pupil.getLastName()).setParameter(4, pupil.getBirthDate(), TemporalType.DATE)
                        .setParameter(5, pupil.getGender().toString())
                        .setParameter(6, pupil.getPhotoLink())
                        .setParameter(7, UserType.PUPIL.toString())
                        .setParameter(8, pupil.getScore())
                        .setParameter(9, pupil.getLevel())
                        .setParameter(10, pupil.getEmail())
                        .getSingleResult();
                em.createNativeQuery(insertAuth).setParameter(1, userid).setParameter(2, hashedPassword).executeUpdate();
                return UUID.fromString(userid);

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
                            pupil.setPhotoLink((String) obj[5]);
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


}
