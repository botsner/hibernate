package ru.botsner.hibernate.hibernate_crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.botsner.hibernate.hibernate_crud.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static EmployeeDAO empDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();
        empDAO = new EmployeeDAO(factory);

        SqlScriptRunner.runSqlScriptFile("/crud/test-data.sql", factory);
    }

    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/crud/reset-autoincrement.sql", factory);
        session = factory.getCurrentSession();
        session.beginTransaction();
    }

    @AfterEach
    public void afterEach() {
        if (session != null) {
            session.getTransaction().rollback();
            session.close();
        }
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void employeeDaoLoads() {
        assertNotNull(empDAO);
    }

    @Test
    void getEmployeesByName() {
        List<Employee> emps = empDAO.getEmployeesByName("Ivan");
        assertNotNull(emps);
        assertFalse(emps.isEmpty());
        emps.forEach(emp -> assertEquals("Ivan", emp.getFirstName()));
    }
}