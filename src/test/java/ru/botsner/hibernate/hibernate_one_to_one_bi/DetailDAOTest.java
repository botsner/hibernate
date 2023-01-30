package ru.botsner.hibernate.hibernate_one_to_one_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Employee;
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
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
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
    void saveDetailWithEmployeeCascade() {
        Employee employee = new Employee("Elena", "Petrova", "HR", 1200);
        Detail detail = new Detail("London", "456789", "elena@gmail.com");

        detail.setEmployee(employee);
        employee.setEmpDetail(detail);

        detDAO.save(detail);

        assertTrue(detail.getId() > 0);
        assertTrue(employee.getId() > 0);
    }

    @Test
    void getDetailWithEmployeeCascade() {
        List<Detail> details = detDAO.findAll();

        assertFalse(details.isEmpty());

        Detail det = details.get(0);
        Employee emp = det.getEmployee();

        assertEquals("Moscow", det.getCity());
        assertEquals("pavel@mail.com", det.getEmail());

        assertNotNull(emp);
        assertEquals("Pavel", emp.getFirstName());
        assertEquals("IT", emp.getDepartment());
    }

    @Test
    void expectedExceptionConstraintViolationWithDeleteDetailById() throws PersistenceException {
        PersistenceException thrown = assertThrows(PersistenceException.class,
                () -> detDAO.deleteById(1));

        assertNotNull(thrown.getMessage());
    }
}