package ru.botsner.hibernate.hibernate_one_to_one_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetailDAOTest {
    private static SessionFactory factory;
    private Session session;

    private static DetailDAO detDAO;
    private static EmployeeDAO empDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Detail.class)
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        detDAO = new DetailDAO(factory);
        empDAO = new EmployeeDAO(factory);

        SqlScriptRunner.runSqlScriptFile("/one_to_one/test-data.sql", factory);
    }

    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/one_to_one/reset-autoincrement.sql", factory);

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
    void detailDaoLoads() {
        assertNotNull(detDAO);
    }

    @Test
    void employeeDaoLoads() {
        assertNotNull(empDAO);
    }

    @Test
    void deleteDetailByEmpIdNoCascade() {
        detDAO.deleteDetailByEmpId(1);
        List<Employee> emps = empDAO.findAll();
        List<Detail> details = detDAO.findAll();

        assertEquals(1, emps.size());
        assertTrue(details.isEmpty());
    }

    @Test
    void expectedExceptionConstraintViolationWhenDeleteDetailById() throws PersistenceException {
        PersistenceException thrown = assertThrows(PersistenceException.class,
                () -> detDAO.deleteById(1));

        assertNotNull(thrown.getMessage());
    }
}