package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    В данном классе устанавливается соединение с БД, задаются константы передаваемые в DriverManager.
    Если происходит SQLException при попытке установить соединение, он будет перехвачен,
    выведено сообщение об ошибке, и исключение будет проброшено дальше для обработки в других частях программы.
 */

public class Util {
//    private static final SessionFactory SESSION_FACTORY;
//
//    static {
//        try {
//            // Загружаем конфигурацию Hibernate из hibernate.cfg.xml
//            SESSION_FACTORY = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
//        } catch (Exception ex) {
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }

    public static Session getSession() {
        return SESSION_FACTORY.openSession();
    }

    private static final SessionFactory SESSION_FACTORY;

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // Загружаем конфигурацию Hibernate из hibernate.cfg.xml
                .build();
        try {
            SESSION_FACTORY = new MetadataSources(registry)
//                    .addAnnotatedClass(User.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }
}

