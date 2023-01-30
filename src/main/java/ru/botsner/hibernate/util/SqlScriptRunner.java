package ru.botsner.hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.InputStream;

public class SqlScriptRunner {
    public static void runSqlScriptFile(String filePath, SessionFactory factory) {
        try (Session session = factory.getCurrentSession();
             InputStream is = SqlScriptRunner.class.getResourceAsStream(filePath)) {

            String sqlScript = new String(is.readAllBytes());

            session.beginTransaction();
            session.createNativeQuery(sqlScript)
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
