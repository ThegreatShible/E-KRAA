package Persistance;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryCreator {
    public static SessionFactory create() {
        SessionFactory session = new Configuration().configure().buildSessionFactory();
        return session;
    }
}
